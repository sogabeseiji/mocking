package com.buildria.restmock.builder.stub;

import com.buildria.restmock.stub.StubHttpServer;
import java.util.Objects;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

/**
 *
 * @author sogabe
 */
public class RequestSpec {

    private final StubHttpServer server;

    private Matcher<?>  path;

    private RequestSpec(StubHttpServer server) {
        this.server = Objects.requireNonNull(server);
    }

    public static RequestSpec when(StubHttpServer server) {
        return new RequestSpec(server);
    }

    public RequestSpec path(String path) {
        return RequestSpec.this.path(equalTo(Objects.requireNonNull(path)));
    }

    public RequestSpec path(Matcher<?> path) {
        this.path = Objects.requireNonNull(path);
        return this;
    }

    public ResponseSpec then() {
        return new ResponseSpec(server, path);
    }

}
