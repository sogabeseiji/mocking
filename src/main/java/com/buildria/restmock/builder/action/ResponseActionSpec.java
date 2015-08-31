package com.buildria.restmock.builder.action;

import com.buildria.restmock.builder.action.Action.Body;
import com.buildria.restmock.builder.action.Action.Header;
import com.buildria.restmock.builder.action.Action.RawBody;
import com.buildria.restmock.builder.action.Action.StatusCode;
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
public class ResponseActionSpec extends ActionSpec {

    private final Matcher<?> path;

    ResponseActionSpec(Matcher<?> path) {
        this.path = path;
    }

    public ResponseActionSpec statusCode(int code) {
        addAction(new StatusCode(path, code));
        return this;
    }

    public ResponseActionSpec contentType(String contentType) {
        return header(CONTENT_TYPE, contentType);
    }

    public ResponseActionSpec contentType(MediaType contentType) {
        return header(CONTENT_TYPE, contentType.toString());
    }

    public ResponseActionSpec header(String name, String value) {
        addAction(new Header(path, name, value));
        return this;
    }

    public ResponseActionSpec rawBody(String content) {
        return rawBody(content, StandardCharsets.UTF_8);
    }

    public ResponseActionSpec rawBody(String content, Charset charset) {
        return rawBody(content.getBytes(charset));
    }

    public ResponseActionSpec rawBody(byte[] content) {
        addAction(new RawBody(path, content));
        return this;
    }

    public ResponseActionSpec rawBody(URL url) throws IOException {
        return rawBody(Resources.toByteArray(url));
    }

    public ResponseActionSpec body(Object content) {
        addAction(new Body(path, content, getActions()));
        return this;
    }

}
