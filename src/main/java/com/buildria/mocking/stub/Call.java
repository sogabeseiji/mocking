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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.buildria.mocking.stub;

import com.google.common.base.MoreObjects;
import com.google.common.net.MediaType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;

public class Call {

    private String path;

    private String method;

    private final List<Pair> headers = new CopyOnWriteArrayList<>();

    private final List<Pair> parameters = new CopyOnWriteArrayList<>();

    private byte[] body = new byte[]{};

    private MediaType contentType;

    private Call() {
        //
    }

    public static Call fromRequest(HttpRequest req) {
        Objects.requireNonNull(req);
        Call call = new Call();

        call.method = req.getMethod().name();
        QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
        call.path = decoder.path();

        Map<String, List<String>> params = decoder.parameters();
        for (String name : params.keySet()) {
            List<String> values = params.get(name);
            for (String value : values) {
                call.parameters.add(new Pair(name, value));
            }
        }

        HttpHeaders headers = req.headers();
        for (String name : headers.names()) {
            List<String> values = headers.getAll(name);
            for (String value : values) {
                call.headers.add(new Pair(name, value));
            }
            if (CONTENT_TYPE.equalsIgnoreCase(name)) {
                call.contentType = MediaType.parse(headers.get(CONTENT_TYPE));
            }
        }

        if (req instanceof ByteBufHolder) {
            ByteBuf buf = ((ByteBufHolder) req).content();
            if (buf != null) {
                call.body = new byte[buf.readableBytes()];
                buf.readBytes(call.body);
            }
        }

        LOG.debug("### call: {}", call.toString());
        return call;
    }

    @Nonnull
    public String getPath() {
        return path;
    }

    @Nonnull
    public String getMethod() {
        return method;
    }

    @Nonnull
    public List<Pair> getHeaders() {
        return new ArrayList<>(headers);
    }

    @Nonnull
    public List<Pair> getParameters() {
        return new ArrayList<>(parameters);
    }

    @Nullable
    public byte[] getBody() {
        return body;
    }

    @Nullable
    public MediaType getContentType() {
        return contentType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).
                add("path", path).
                add("method", method).
                add("headers", headers).
                add("parameters", parameters).
                add("body", DatatypeConverter.printHexBinary(body)).
                toString();
    }

    private static final Logger LOG = LoggerFactory.getLogger(Call.class);
}
