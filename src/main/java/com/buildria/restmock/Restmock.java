package com.buildria.restmock;

import com.buildria.restmock.builder.action.ActionSpec;
import com.buildria.restmock.builder.rule.RuleSpec;
import com.buildria.restmock.stub.StubHttpServer;
import javax.annotation.Nonnull;
import org.junit.rules.ExternalResource;

public class Restmock extends ExternalResource {

    public static final int PORT = 8888;

    private StubHttpServer server;

    private final int port;

    public Restmock() {
        this(PORT);
    }

    public Restmock(int port) {
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    // CHECKSTYLE:OFF
    @Override
    protected void before() throws Throwable {
    // CHECKSTYLE:ON
        super.before();
        server = new StubHttpServer(port).run();
    }

    @Override
    protected void after() {
        if (server != null) {
            server.stop();
            server = null;
        }
        super.after();
    }

    @Nonnull
    public void when(ActionSpec spec) {
        server.addActions(spec.getActions());
    }

    @Nonnull
    public void verify(RuleSpec spec) {
        spec.validate(server.getCalls());
    }
}
