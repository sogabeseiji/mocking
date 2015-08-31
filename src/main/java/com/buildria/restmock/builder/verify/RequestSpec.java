package com.buildria.restmock.builder.verify;

import com.buildria.restmock.builder.verify.Rule.Header;
import com.buildria.restmock.builder.verify.Rule.Parameter;
import com.buildria.restmock.stub.Call;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.Joiner;
import com.google.common.net.MediaType;
import java.util.List;
import org.hamcrest.Matcher;

import static com.buildria.restmock.http.RMHttpHeaders.ACCEPT;
import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.equalTo;

public class RequestSpec {

    private final StubHttpServer server;

    private List<Call> calls;

    @SuppressWarnings("PMD.UnusedPrivateField")
    private final String path;

    RequestSpec(StubHttpServer server, List<Call> calls, String path) {
        this.server = server;
        this.calls = calls;
        this.path = path;
    }

    public RequestSpec header(String name, Matcher<?> value) {
        calls = Calls.filter(calls, new Header(server, path, name, value));
        if (calls.isEmpty()) {
            throw new AssertionError(
                    String.format("No calls found. header: %s value: %s", name, value.toString()));
        }
        return this;
    }

    public RequestSpec header(String name, String value) {
        return header(name, equalTo(value));
    }

    public RequestSpec header(String name, MediaType value) {
        return header(name, value.toString());
    }

    public RequestSpec contentType(Matcher<?> value) {
        return header(CONTENT_TYPE, value);
    }

    public RequestSpec contentType(String value) {
        return header(CONTENT_TYPE, value);
    }

    public RequestSpec contentType(MediaType value) {
        return header(CONTENT_TYPE, value);
    }

    public RequestSpec accept(Matcher<?> value) {
        return header(ACCEPT, value);
    }

    public RequestSpec accept(String value) {
        return header(ACCEPT, value);
    }

    public RequestSpec accept(MediaType value) {
        return header(ACCEPT, value);
    }

    public RequestSpec parameter(String key, String value) {
        return parameters(key, new String[]{value});
    }

    public RequestSpec parameters(String key, String[] values) {
        calls = Calls.filter(calls, new Parameter(server, path, key, values));
        if (calls.isEmpty()) {
            throw new AssertionError(
                    String.format("No calls found. key: %s value: %s", key, Joiner.on(", ").join(values)));
        }
        return this;
    }

    public RequestSpec queryParam(String key, String value) {
        return parameter(key, value);
    }

    public RequestSpec queryParams(String key, String... values) {
        return parameters(key, values);
    }

}
