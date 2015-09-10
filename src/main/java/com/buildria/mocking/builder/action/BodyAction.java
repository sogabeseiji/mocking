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

import com.buildria.mocking.MockingException;
import com.buildria.mocking.serializer.ObjectSerializer;
import com.buildria.mocking.serializer.ObjectSerializerContext;
import com.buildria.mocking.serializer.ObjectSerializerFactory;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;

/**
 * BodyAction.
 */
public class BodyAction extends BaseAction {

    private final Object content;

    private final List<Action> actions;

    public BodyAction(@Nonnull String path, @Nonnull Object content,
            @Nonnull List<Action> actions) {
        super(path);
        this.content = Objects.requireNonNull(content);
        this.actions = Objects.requireNonNull(actions);
    }

    @Nonnull
    @Override
    public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
        Objects.requireNonNull(req);
        Objects.requireNonNull(res);
        HeaderAction contentType = getHeader(req.getUri(), CONTENT_TYPE, actions);
        if (contentType == null) {
            throw new MockingException("No Content-Type found.");
        }
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(contentType.getValue());
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        try {
            return new RawBodyAction(getPath(),
                    os.serialize(content)).apply(req, res);
        } catch (IOException ex) {
            throw new MockingException("failed to serialize body.");
        }
    }

    @Nullable
    private HeaderAction getHeader(String uri, String headerName, List<Action> actions) {
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

}
