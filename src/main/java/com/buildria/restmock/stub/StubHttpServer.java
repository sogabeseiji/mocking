package com.buildria.restmock.stub;

import com.buildria.restmock.builder.stub.Scenario;
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
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sogabe
 */
public class StubHttpServer {

    private final int port;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private final List<Scenario> scenarios = new CopyOnWriteArrayList<>();

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

    public void addScenario(Scenario scenario) {
        scenarios.add(scenario);
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
        LOG.info("### It took {} ms", System.currentTimeMillis() - start);
    }

    private class Handler extends SimpleChannelInboundHandler<Object> {

        @Override
        public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
            LOG.info("### req {}", msg.getClass().getName());
            HttpRequest req = (HttpRequest) msg;
            LOG.info("### called. Request: {}", req.toString());
            HttpResponse response = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            boolean proceed = false;
            for (Scenario scenario : scenarios) {
                if (scenario.isApplicable(req.getUri())) {
                    LOG.info("### {} matched", req.getUri());
                    proceed = true;
                    response = scenario.apply(response);
                }
            }
            if (!proceed) {
                response.setStatus(HttpResponseStatus.NOT_FOUND);
            }

            LOG.info("### Response: {}", response.toString());
            ctx.writeAndFlush(response);
            ctx.channel().close();
        }

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            super.exceptionCaught(ctx, cause); //To change body of generated methods, choose Tools | Templates.
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(StubHttpServer.class);
}
