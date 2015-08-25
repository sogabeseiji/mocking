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

    private Matcher<?>  uri;

    public RequestSpec(StubHttpServer server) {
        this.server = Objects.requireNonNull(server);
    }

    public static RequestSpec when(StubHttpServer server) {
        return new RequestSpec(server);
    }

    public RequestSpec uri(String uri) {
        return uri(equalTo(Objects.requireNonNull(uri)));
    }

    public RequestSpec uri(Matcher<?> uri) {
        this.uri = Objects.requireNonNull(uri);
        return this;
    }

    public ResponseSpec then() {
        return new ResponseSpec(server, uri);
    }

}
