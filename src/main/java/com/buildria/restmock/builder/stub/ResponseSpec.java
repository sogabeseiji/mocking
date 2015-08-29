package com.buildria.restmock.builder.stub;

import com.buildria.restmock.builder.stub.Action.BodyAction;
import com.buildria.restmock.builder.stub.Action.HeaderAction;
import com.buildria.restmock.builder.stub.Action.RawBodyAction;
import com.buildria.restmock.builder.stub.Action.StatusCodeAction;
import com.buildria.restmock.http.HttpHeader;
import com.buildria.restmock.http.HttpStatus;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.io.Resources;
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

    private final Matcher<?> path;

    ResponseSpec(StubHttpServer server, Matcher<?> path) {
        this.server = server;
        this.path = path;
    }

    public ResponseSpec statusCode(int code) {
        server.addAction(new StatusCodeAction(server, path, code));
        return this;
    }

    public ResponseSpec statusCode(HttpStatus status) {
        return statusCode(status.getCode());
    }

    public ResponseSpec contentType(String contentType) {
        return header(HttpHeader.CONTENT_TYPE, contentType);
    }

    public ResponseSpec contentType(MediaType contentType) {
        return header(HttpHeader.CONTENT_TYPE, contentType.toString());
    }

    public ResponseSpec header(String name, String value) {
        server.addAction(new HeaderAction(server, path, name, value));
        return this;
    }

    public ResponseSpec rawBody(String content) {
        return rawBody(content, StandardCharsets.UTF_8);
    }

    public ResponseSpec rawBody(String content, Charset charset) {
        return rawBody(content.getBytes(charset));
    }

    public ResponseSpec rawBody(byte[] content) {
        server.addAction(new RawBodyAction(server, path, content));
        return this;
    }

    public ResponseSpec rawBody(URL url) throws IOException {
        return rawBody(Resources.toByteArray(url));
    }

    public ResponseSpec body(Object content) {
        server.addAction(new BodyAction(server, path, content));
        return this;
    }

}
