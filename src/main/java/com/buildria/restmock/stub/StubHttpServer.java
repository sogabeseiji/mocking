package com.buildria.restmock.stub;

import com.buildria.restmock.builder.stub.Action;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * StubHttpServer
 *
 * @author Seiji Sogabe
 */
public class StubHttpServer {

    private final int port;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private final List<Action> actions = new CopyOnWriteArrayList<>();

    private final List<Call> calls = new CopyOnWriteArrayList<>();

    private final Object lockObj = new Object();

    public StubHttpServer() {
        this(8080);
    }

    public StubHttpServer(int port) {
        this.port = port;
    }

    public StubHttpServer run() throws Exception {
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline().addLast("decoder", new HttpRequestDecoder(4096, 8192, 8192));
                        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(8192));
                        ch.pipeline().addLast("encoder", new HttpResponseEncoder());
                        ch.pipeline().addLast("handler", new Handler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, 128)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        ChannelFuture f = b.bind(port).sync();
        f.awaitUninterruptibly();
        return this;
    }

    public List<Call> getCalls() {
        return Collections.unmodifiableList(calls);
    }

    public void addAction(Action action) {
        synchronized (lockObj) {
            actions.add(action);
        }
    }

    public List<Action> getActions() {
        synchronized (lockObj) {
            return Collections.unmodifiableList(actions);
        }
    }

    public void clear() {
        synchronized (lockObj) {
            actions.clear();
        }
    }

    public void stop() {
        if (workerGroup != null) {
            workerGroup.shutdownGracefully();
        }
        if (bossGroup != null) {
            bossGroup.shutdownGracefully();
        }
    }

    public static void main(String[] args) throws Exception {
        long start = System.currentTimeMillis();
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        StubHttpServer server = new StubHttpServer(port);
        server.run();
        LOG.debug("### It took {} ms", System.currentTimeMillis() - start);
    }

    private class Handler extends SimpleChannelInboundHandler<Object> {

        @Override
        public void channelRead0(final ChannelHandlerContext ctx, Object msg) throws Exception {
            if (!(msg instanceof HttpRequest)) {
                return;
            }

            HttpRequest req = (HttpRequest) msg;
            calls.add(Call.fromRequest(req));

            HttpResponse response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            boolean proceed = false;
            for (Action action : actions) {
                if (action.isApplicable(req.getUri())) {
                    proceed = true;
                    response = action.apply(response);
                }
            }
            if (!proceed) {
                response.setStatus(HttpResponseStatus.NOT_FOUND);
            }

            final HttpResponse r = response;
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    ctx.writeAndFlush(r);
                }
            });

        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause);
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(StubHttpServer.class);
}
