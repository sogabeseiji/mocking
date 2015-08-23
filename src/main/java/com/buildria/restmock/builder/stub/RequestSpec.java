package com.buildria.restmock.builder.stub;

import com.buildria.restmock.stub.StubHttpServer;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 *
 * @author sogabe
 */
public class RequestSpec {

    private final StubHttpServer server;

    private Matcher<?>  uri;

    public RequestSpec(StubHttpServer server) {
        this.server = server;
    }

    public static RequestSpec when(StubHttpServer server) {
        return new RequestSpec(server);
    }

    public RequestSpec uri(String uri) {
        this.uri = Matchers.equalTo(uri);
        return this;
    }

    public RequestSpec uri(Matcher<?> uri) {
        this.uri = uri;
        return this;
    }

    public ResponseSpec then() {
        return new ResponseSpec(server, uri);
    }

}
