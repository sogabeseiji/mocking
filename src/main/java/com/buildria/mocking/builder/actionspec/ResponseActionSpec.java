package com.buildria.mocking.builder.actionspec;

import com.buildria.mocking.builder.actionspec.action.BodyAction;
import com.buildria.mocking.builder.actionspec.action.DelayAction;
import com.buildria.mocking.builder.actionspec.action.HeaderAction;
import com.buildria.mocking.builder.actionspec.action.RawBodyAction;
import com.buildria.mocking.builder.actionspec.action.StatusCodeAction;
import com.google.common.io.ByteStreams;
import com.google.common.io.Resources;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.hamcrest.Matcher;

import static com.buildria.mocking.http.RMHttpHeaders.CONTENT_TYPE;

/**
 *
 * @author sogabe
 */
public class ResponseActionSpec extends ActionSpec {

    private final Matcher<?> path;

    ResponseActionSpec(Matcher<?> path) {
        this.path = path;
    }

    public ResponseActionSpec statusCode(int code) {
        addAction(new StatusCodeAction(path, code));
        return this;
    }

    public ResponseActionSpec contentType(String contentType) {
        return header(CONTENT_TYPE, contentType);
    }

    public ResponseActionSpec header(String name, String value) {
        addAction(new HeaderAction(path, name, value));
        return this;
    }

    public ResponseActionSpec rawBody(String content) {
        return rawBody(content, StandardCharsets.UTF_8);
    }

    public ResponseActionSpec rawBody(String content, Charset charset) {
        return rawBody(content.getBytes(charset));
    }

    public ResponseActionSpec rawBody(byte[] content) {
        addAction(new RawBodyAction(path, content));
        return this;
    }

    public ResponseActionSpec rawBody(URL url) throws IOException {
        return rawBody(Resources.toByteArray(url));
    }

    public ResponseActionSpec rawBody(InputStream is) throws IOException {
        return rawBody(ByteStreams.toByteArray(is));
    }

    public ResponseActionSpec body(Object content) {
        addAction(new BodyAction(path, content, getActions()));
        return this;
    }

    public ResponseActionSpec delay(long wait) {
        addAction(new DelayAction(path, wait));
        return this;
    }

}
