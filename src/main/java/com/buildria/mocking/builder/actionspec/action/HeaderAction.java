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
package com.buildria.mocking.builder.actionspec.action;

import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import java.util.Objects;
import javax.annotation.Nonnull;

/**
 * HeaderAction.
 */
public class HeaderAction extends Action {

    private final String header;

    private final String value;

    @Nonnull
    public HeaderAction(@Nonnull String path, @Nonnull String header,
            @Nonnull String value) {
        super(path);
        this.header = Objects.requireNonNull(header);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public String getHeader() {
        return header;
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Nonnull
    @Override
    public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
        Objects.requireNonNull(req);
        Objects.requireNonNull(res);
        res.headers().add(header, value);
        return res;
    }

}
