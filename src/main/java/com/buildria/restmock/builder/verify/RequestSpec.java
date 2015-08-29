package com.buildria.restmock.builder.verify;

import com.buildria.restmock.builder.verify.Verifier.Header;
import com.buildria.restmock.stub.Call;
import com.google.common.net.MediaType;
import java.util.List;

public class RequestSpec {

    private final List<Call> calls;

    private final String uri;

    RequestSpec(List<Call> calls, String uri) {
        this.calls = calls;
        this.uri = uri;
    }

    public RequestSpec header(String name, String value) {
        List<Call> answers = CallsVerifier.verify(calls, new Header(name, value));
        return new RequestSpec(answers, uri);
    }

    public RequestSpec header(String name, MediaType value) {
        return header(name, value.toString());
    }

    public RequestSpec contentType(String value) {
        return header("Cotent-Type", value);
    }

    public RequestSpec contentType(MediaType value) {
        return header("Cotent-Type", value);
    }

    public RequestSpec accept(String value) {
        return header("Accept", value);
    }

    public RequestSpec accept(MediaType value) {
        return header("Accept", value);
    }

}
