package com.buildria.restmock.builder.verify;

import com.buildria.restmock.builder.verify.Verifier.Header;
import com.buildria.restmock.builder.verify.Verifier.Parameter;
import com.buildria.restmock.stub.Call;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import java.util.List;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

public class RequestSpec {

    private  List<Call> calls;

    private final String path;

    RequestSpec(List<Call> calls, String path) {
        this.calls = calls;
        this.path = path;
    }

    public RequestSpec header(String name, Matcher<?> value) {
        this.calls = CallsVerifier.verify(calls, new Header(name, value));
        return this;
    }

    public RequestSpec header(String name, String value) {
        return header(name, equalTo(value));
    }

    public RequestSpec header(String name, MediaType value) {
        return header(name, value.toString());
    }

    public RequestSpec contentType(Matcher<?> value) {
        return header(HttpHeaders.CONTENT_TYPE, value);
    }

    public RequestSpec contentType(String value) {
        return header(HttpHeaders.CONTENT_TYPE, value);
    }

    public RequestSpec contentType(MediaType value) {
        return header(HttpHeaders.CONTENT_TYPE, value);
    }

    public RequestSpec accept(Matcher<?> value) {
        return header(HttpHeaders.ACCEPT, value);
    }

    public RequestSpec accept(String value) {
        return header(HttpHeaders.ACCEPT, value);
    }

    public RequestSpec accept(MediaType value) {
        return header(HttpHeaders.ACCEPT, value);
    }

    public RequestSpec parameter(String key, String value) {
        return parameters(key, new String[]{value});
    }

    public RequestSpec parameters(String key, String[] values) {
        this.calls = CallsVerifier.verify(calls, new Parameter(key, values));
        return this;
    }

    public RequestSpec queryParam(String key, String value) {
        return parameter(key, value);
    }

    public RequestSpec queryParams(String key, String... values) {
        return parameters(key, values);
    }

}
