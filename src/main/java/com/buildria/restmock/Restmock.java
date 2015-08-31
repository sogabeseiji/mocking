package com.buildria.restmock;

import com.buildria.restmock.builder.stub.RequestSpec;
import com.buildria.restmock.builder.verify.Spec;
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

    // TODO
    @Nonnull
    public StubHttpServer getServer() {
        return server;
    }

    @Override
    protected void before() throws Throwable {
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
    public  RequestSpec when() {
        return RequestSpec.when(server);
    }

    @Nonnull
    public void verify(Spec spec) {
        spec.validate(server.getCalls());
    }
}
