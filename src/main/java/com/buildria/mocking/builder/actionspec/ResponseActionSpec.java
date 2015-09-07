/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
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

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;

public class ResponseActionSpec extends ActionSpec {

    ResponseActionSpec(Matcher<?> path) {
        super(path);
    }

    public ResponseActionSpec statusCode(int code) {
        addAction(new StatusCodeAction(getPath(), code));
        return this;
    }

    public ResponseActionSpec contentType(String contentType) {
        return header(CONTENT_TYPE, contentType);
    }

    public ResponseActionSpec header(String name, String value) {
        addAction(new HeaderAction(getPath(), name, value));
        return this;
    }

    public ResponseActionSpec rawBody(String content) {
        return rawBody(content, StandardCharsets.UTF_8);
    }

    public ResponseActionSpec rawBody(String content, Charset charset) {
        return rawBody(content.getBytes(charset));
    }

    public ResponseActionSpec rawBody(byte[] content) {
        addAction(new RawBodyAction(getPath(), content));
        return this;
    }

    public ResponseActionSpec rawBody(URL url) throws IOException {
        return rawBody(Resources.toByteArray(url));
    }

    public ResponseActionSpec rawBody(InputStream is) throws IOException {
        return rawBody(ByteStreams.toByteArray(is));
    }

    public ResponseActionSpec body(Object content) {
        addAction(new BodyAction(getPath(), content, getActions()));
        return this;
    }

    public ResponseActionSpec delay(long wait) {
        addAction(new DelayAction(getPath(), wait));
        return this;
    }

}
