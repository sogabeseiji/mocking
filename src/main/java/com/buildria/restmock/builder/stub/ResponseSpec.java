package com.buildria.restmock.builder.stub;

import com.buildria.restmock.http.HttpStatus;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.io.Resources;
import com.google.common.net.HttpHeaders;
import com.google.common.net.MediaType;
import java.io.IOException;
import java.net.URL;
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
        server.addAction(Action.status(server, uri, code));
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
        server.addAction(Action.header(server, uri, name, value));
        return this;
    }

    public ResponseSpec body(String content) {
        return body(content, StandardCharsets.UTF_8);
    }

    public ResponseSpec body(String content, Charset charset) {
        return body(content.getBytes(charset));
    }

    public ResponseSpec body(byte[] content) {
        server.addAction(Action.body(server, uri, content));
        return this;
    }

    public ResponseSpec body(URL url) throws IOException {
        return body(Resources.toByteArray(url));
    }
}
