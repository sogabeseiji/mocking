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

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// CHECKSTYLE:OFF
public abstract class Action {
// CHECKSTYLE:ON

    private final String path;

    public Action(@Nonnull String path) {
        this.path = Objects.requireNonNull(path);
    }

    @Nonnull
    public String getPath() {
        return path;
    }

    public boolean isApplicable(String path) {
        return this.path.equals(path);
    }

    @Nullable
    protected HeaderAction getHeader(String uri, String headerName, List<Action> actions) {
        QueryStringDecoder decoder = new QueryStringDecoder(uri);
        String p = QueryStringDecoder.decodeComponent(decoder.path());
        for (Action action : actions) {
            if (action.isApplicable(p) && action instanceof HeaderAction) {
                HeaderAction ha = (HeaderAction) action;
                if (ha.getHeader().equalsIgnoreCase(headerName)) {
                    return ha;
                }
            }
        }
        return null;
    }

    @Nonnull
    public abstract HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res);

    public ToStringHelper objects() {
        return MoreObjects.toStringHelper(this).add("path", path);
    }

    @Override
    public String toString() {
        return objects().toString();
    }

}
