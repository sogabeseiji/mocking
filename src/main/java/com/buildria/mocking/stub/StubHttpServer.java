/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buildria.mocking.stub;

import com.buildria.mocking.Mocking;
import com.buildria.mocking.MockingException;
import com.buildria.mocking.builder.action.Action;
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
import io.netty.handler.logging.LoggingHandler;
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
public class StubHttpServer implements Server {

    private static final int MAX_INITIALLINE_LENGH = 4096;

    private static final int MAX_HEADERS_SIZE = 8192;

    private static final int MAX_CHUNK_SIZE = 8192;

    private static final int MAX_CONTENT_LENGTH = 8192;

    private static final int SO_BACKLOG = 128;

    private EventLoopGroup bossGroup;

    private EventLoopGroup workerGroup;

    private final List<Action> actions = new CopyOnWriteArrayList<>();

    private final List<Call> calls = new CopyOnWriteArrayList<>();

    private final Object lockObj = new Object();

    private final Mocking mocking;

    public StubHttpServer(Mocking mocking) {
        this.mocking = mocking;
    }

    @Override
    public StubHttpServer start() {
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
                        if (mocking.isLogging()) {
                            ch.pipeline().addLast("logging", new LoggingHandler(StubHttpServer.class));
                        }
                        ch.pipeline().addLast("handler", new Handler());
                    }
                })
                .option(ChannelOption.SO_BACKLOG, SO_BACKLOG)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // Bind and start to accept incoming connections.
        int port = mocking.getPort();
        ChannelFuture f;
        try {
            f = b.bind(port).sync();
        } catch (InterruptedException ex) {
            throw new MockingException(ex);
        }
        f.awaitUninterruptibly();
        sw.stop();
        LOG.debug("### StubHttpServer(port:{}) started. It took {}", port, sw);
        return this;
    }

    @Override
    public List<Call> getCalls() {
        return calls;
    }

    @Override
    public void addAction(Action action) {
        Objects.requireNonNull(action);
        synchronized (lockObj) {
            actions.add(action);
        }
    }

    @Override
    public void addActions(List<Action> actions) {
        Objects.requireNonNull(actions);
        synchronized (lockObj) {
            for (Action action : actions) {
                this.actions.add(action);
            }
        }
    }

    @Override
    public List<Action> getActions() {
        synchronized (lockObj) {
            return Collections.unmodifiableList(actions);
        }
    }

    @Override
    public void stop() {
        workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
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
            Call call = Call.fromRequest(req);
            calls.add(call);

            HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
            boolean proceed = false;
            String path = call.getPath();
            for (Action action : actions) {
                if (action.isApplicable(path)) {
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
