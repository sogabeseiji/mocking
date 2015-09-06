package com.buildria.mocking;

import com.buildria.mocking.builder.actionspec.ActionSpec;
import com.buildria.mocking.builder.rulespec.RuleSpec;
import com.buildria.mocking.stub.StubHttpServer;
import javax.annotation.Nonnull;
import org.junit.rules.ExternalResource;

public class Mocking extends ExternalResource {

    public static final int PORT = 8888;

    private StubHttpServer server;

    private final int port;

    public Mocking() {
        this(PORT);
    }

    public Mocking(int port) {
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
    public void $(ActionSpec spec) {
        server.addActions(spec.getActions());
    }

    @Nonnull
    public void $(RuleSpec spec) {
        spec.validate(server.getCalls());
    }
}
