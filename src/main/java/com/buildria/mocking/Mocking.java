package com.buildria.mocking;

import com.buildria.mocking.builder.actionspec.ActionSpec;
import com.buildria.mocking.builder.rulespec.RuleSpec;
import com.buildria.mocking.stub.StubHttpServer;
import javax.annotation.Nonnull;
import org.junit.rules.ExternalResource;

public class Mocking extends ExternalResource {

    public static final int PORT = 8888;

    private static final int PORT_MIN = 0;

    private static final int PORT_MAX = 65535;

    private StubHttpServer server;

    private int port = PORT;

    private boolean logging;

    public Mocking() {
        super();
    }

    // CHECKSTYLE:OFF
    @Override
    protected void before() throws Throwable {
        // CHECKSTYLE:ON
        super.before();
        server = new StubHttpServer(this).run();
    }

    public int getPort() {
        return port;
    }

    public boolean isLogging() {
        return logging;
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

    @Nonnull
    public Mocking port(int port) {
        if (port < PORT_MIN || port > PORT_MAX) {
            throw new IllegalArgumentException("port should be between 0 and 65535");
        }
        this.port = port;
        return this;
    }

    @Nonnull
    public Mocking logging(boolean logging) {
        this.logging = logging;
        return this;
    }
}
