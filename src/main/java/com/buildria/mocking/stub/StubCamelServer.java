package com.buildria.mocking.stub;

import com.buildria.mocking.Mocking;
import com.buildria.mocking.MockingException;
import com.buildria.mocking.builder.action.Action;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.netty4.http.NettyHttpMessage;
import org.apache.camel.main.Main;

public class StubCamelServer implements StubServer {

    private final List<Action> actions = new CopyOnWriteArrayList<>();

    private final List<Call> calls = new CopyOnWriteArrayList<>();

    private final Mocking mocking;

    private final Object lockObj = new Object();

    private Main main;

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

    public StubCamelServer(Mocking mocking) {
        this.mocking = mocking;
    }

    @Override
    public StubCamelServer start() throws MockingException {
        main = new Main();
        main.addRouteBuilder(new RouteBUider());
        try {
            main.start();
        } catch (Exception ex) {
            throw new MockingException(ex);
        }
        return this;
    }

    @Override
    public void stop() {
        try {
            main.shutdown();
        } catch (Exception ex) {
            //
        }
    }

    private class RouteBUider extends RouteBuilder {

        @Override
        public void configure() throws Exception {
            String url = "http://127.0.0.1:" + mocking.getPort() + "?matchOnUriPrefix=true";
            from("netty4-http:" + url).process(new Processor() {
                @Override
                public void process(Exchange exchange) throws Exception {
                    HttpRequest req = exchange.getIn(NettyHttpMessage.class).getHttpRequest();
                    calls.add(Call.fromRequest(req));
                    HttpResponse res = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1,
                            HttpResponseStatus.OK);
                    boolean proceed = false;
                    String path = exchange.getIn().getHeader(Exchange.HTTP_PATH, String.class);
                    for (Action action : actions) {
                        if (action.isApplicable(path)) {
                            proceed = true;
                            res = action.apply(req, res);
                        }
                    }
                    if (!proceed) {
                        exchange.getIn().setHeader(
                                Exchange.HTTP_RESPONSE_CODE, HttpResponseStatus.NOT_FOUND);
                        return;
                    }
                    exchange.getIn().setBody(res);
                }
            });
        }

    }
}
