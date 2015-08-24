package com.buildria.restmock.builder.stub;

import com.buildria.restmock.http.HttpStatus;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.hamcrest.Matcher;

/**
 *
 * @author sogabe
 */
public class ResponseSpec {

    private final StubHttpServer server;

    private final Matcher<?> uri;

    public ResponseSpec(StubHttpServer server, Matcher<?> uri) {
        this.server = server;
        this.uri = uri;
    }

    public ResponseSpec statusCode(int code) {
        server.addScenario(Scenario.status(uri, code));
        return this;
    }

    public ResponseSpec statusCode(HttpStatus status) {
        return statusCode(status.getCode());
    }

    public ResponseSpec contentType(String contentType) {
        return header(HttpHeaders.CONTENT_TYPE, contentType);
    }

    public ResponseSpec contentType(MediaType contentType) {
        return header(HttpHeaders.CONTENT_TYPE, contentType.toString());
    }

    public ResponseSpec header(String name, String value) {
        server.addScenario(Scenario.header(uri, name, value));
        return this;
    }

    public ResponseSpec body(String content) {
        return body(content, StandardCharsets.UTF_8);
    }

    public ResponseSpec body(String content, Charset charset) {
        server.addScenario(Scenario.body(uri, content, charset));
        return this;
    }

    public ResponseSpec body(byte[] content) {
        server.addScenario(Scenario.body(uri, content));
        return this;
    }

}
