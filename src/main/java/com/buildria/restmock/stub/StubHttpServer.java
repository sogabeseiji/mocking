package com.buildria.restmock.stub;

import com.buildria.restmock.builder.stub.Action;
import com.google.common.base.Stopwatch;
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
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Stopwatch.createStarted;

/**
 * StubHttpServer
 *
 * @author Seiji Sogabe
 */
public class StubHttpServer {

    /**
     * Default port.
     */
    public static final int DEFAULT_PORT = 8080;

    private static final int MAX_INITIALLINE_LENGH = 4096;

    private static final int MAX_HEADERS_SIZE = 8192;

    private static final int MAX_CHUNK_SIZE = 8192;

    private static final int MAX_CONTENT_LENGTH = 8192;

    private static final int SO_BACKLOG = 128;

    private final int port;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private final List<Action> actions = new CopyOnWriteArrayList<>();

    private final List<Call> calls = new CopyOnWriteArrayList<>();

    private final Object lockObj = new Object();

    public StubHttpServer() {
        this(DEFAULT_PORT);
    }

    public StubHttpServer(int port) {
        this.port = port;
    }

    public StubHttpServer run() throws InterruptedException {
        Stopwatch sw = createStarted();
        bossGroup = new NioEventLoopGroup();
        workerGroup = new NioEventLoopGroup();

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel.class)
                .childHandler(new ChannelInitializer<SocketChannel>() {
                    // CHECKSTYLE:OFF
                    @Override
                    public void initChannel(SocketChannel ch) throws Exception {
                    // CHECKSTYLE:ON
                        // int maxInitialLineLength, int maxHeaderSize, int maxChunkSize
                        ch.pipeline().addLast("decoder",
                                new HttpRequestDecoder(MAX_INITIALLINE_LENGH, MAX_HEADERS_SIZE, MAX_CHUNK_SIZE));
                        ch.pipeline().addLast("aggregator", new HttpObjectAggregator(MAX_CONTENT_LENGTH));
                        ch.pipeline().addLast("encoder", new HttpResponseEncoder());
                        ch.pipeline().addLast("handler", new Handler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, SO_BACKLOG)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        ChannelFuture f = b.bind(port).sync();
        f.awaitUninterruptibly();
        sw.stop();
        LOG.debug("### StubHttpServer(port:{}) started. It took {}", port, sw);
        return this;
    }

    public List<Call> getCalls() {
        return Collections.unmodifiableList(calls);
    }

    public void addAction(Action action) {
        Objects.requireNonNull(action);
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
        LOG.debug("### StubHttpServer stopped.");
    }

    private class Handler extends SimpleChannelInboundHandler<Object> {

        // CHECKSTYLE:OFF
        @Override
        public void channelRead0(final ChannelHandlerContext ctx, Object msg) throws Exception {
        // CHECKSTYLE:ON
            if (!(msg instanceof HttpRequest)) {
                return;
            }

            HttpRequest req = (HttpRequest) msg;
            calls.add(Call.fromRequest(req));

            HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            boolean proceed = false;
            for (Action action : actions) {
                if (action.isApplicable(req.getUri())) {
                    proceed = true;
                    res = action.apply(req, res);
                }
            }
            if (!proceed) {
                res.setStatus(HttpResponseStatus.NOT_FOUND);
            }

            final HttpResponse r = res;
            ctx.channel().eventLoop().execute(new Runnable() {
                @Override
                public void run() {
                    ctx.writeAndFlush(r);
                }
            });

        }

        // CHECKSTYLE:OFF
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        // CHECKSTYLE:ON
            super.exceptionCaught(ctx, cause);
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(StubHttpServer.class);
}
