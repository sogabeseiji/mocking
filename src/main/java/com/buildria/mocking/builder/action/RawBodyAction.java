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
package com.buildria.mocking.builder.action;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_LENGTH;

/**
 * RawBodyAction.
 */
public class RawBodyAction extends Action {

    private final byte[] content;

    public RawBodyAction(@Nonnull String path, @Nonnull byte[] content) {
        super(path);
        this.content = Objects.requireNonNull(content);
    }

    @Nonnull
    @Override
    public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
        Objects.requireNonNull(req);
        Objects.requireNonNull(res);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(content.length);
        buffer.writeBytes(content);
        HttpResponse r = new DefaultFullHttpResponse(res.getProtocolVersion(), res.getStatus(), buffer);
        for (Map.Entry<String, String> entry : res.headers()) {
            r.headers().add(entry.getKey(), entry.getValue());
        }
        r.headers().remove(CONTENT_LENGTH);
        r.headers().add(CONTENT_LENGTH, content.length);
        return r;
    }

}
