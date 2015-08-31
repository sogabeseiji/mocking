package com.buildria.restmock.builder.stub;

import com.buildria.restmock.builder.stub.Action.BodyAction;
import com.buildria.restmock.builder.stub.Action.HeaderAction;
import com.buildria.restmock.builder.stub.Action.RawBodyAction;
import com.buildria.restmock.builder.stub.Action.StatusCodeAction;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.hamcrest.Matcher;

import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;

/**
 *
 * @author sogabe
 */
public class ResponseSpec extends Spec {

    private final Matcher<?> path;

    ResponseSpec(Matcher<?> path) {
        this.path = path;
    }

    public ResponseSpec statusCode(int code) {
        addAction(new StatusCodeAction(path, code));
        return this;
    }

    public ResponseSpec contentType(String contentType) {
        return header(CONTENT_TYPE, contentType);
    }

    public ResponseSpec contentType(MediaType contentType) {
        return header(CONTENT_TYPE, contentType.toString());
    }

    public ResponseSpec header(String name, String value) {
        addAction(new HeaderAction(path, name, value));
        return this;
    }

    public ResponseSpec rawBody(String content) {
        return rawBody(content, StandardCharsets.UTF_8);
    }

    public ResponseSpec rawBody(String content, Charset charset) {
        return rawBody(content.getBytes(charset));
    }

    public ResponseSpec rawBody(byte[] content) {
        addAction(new RawBodyAction(path, content));
        return this;
    }

    public ResponseSpec rawBody(URL url) throws IOException {
        return rawBody(Resources.toByteArray(url));
    }

    public ResponseSpec body(Object content) {
        addAction(new BodyAction(path, content, getActions()));
        return this;
    }

}
