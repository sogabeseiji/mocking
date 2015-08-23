package com.buildria.restmock.builder.stub;

import com.buildria.restmock.builder.stub.Scenario.Body;
import com.buildria.restmock.builder.stub.Scenario.Header;
import com.buildria.restmock.builder.stub.Scenario.Status;
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

    public ResponseSpec status(int code) {
        server.addScenario(new Status(uri, code));
        return this;
    }

    public ResponseSpec status(HttpStatus status) {
        server.addScenario(new Status(uri, status.getCode()));
        return this;
    }

    public ResponseSpec contentType(String contentType) {
        return header(HttpHeaders.CONTENT_TYPE, contentType);
    }

    public ResponseSpec contentType(MediaType contentType) {
        return header(HttpHeaders.CONTENT_TYPE, contentType.toString());
    }

    public ResponseSpec header(String name, String value) {
        server.addScenario(new Header(uri, name, value));
        return this;
    }

    public ResponseSpec body(String content) {
        return body(content, StandardCharsets.UTF_8);
    }

    public ResponseSpec body(String content, Charset charset) {
        server.addScenario(new Body(uri, content, charset));
        return this;
    }

}
