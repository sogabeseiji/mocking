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
package com.buildria.mocking.serializer;

import com.buildria.mocking.MockingException;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.Nonnull;

public class ObjectSerializerContext {

    private static final String JACKSON_CLASS
            = "com/fasterxml/jackson/databind/ObjectMapper.class";

    private static final String GSON_CLASS = "com/google/gson/Gson.class";

    private final String contentType;

    private final String type;

    private final String subtype;

    private final Charset charset;

    public ObjectSerializerContext(String contentType) {
        this.contentType = Objects.requireNonNull(contentType);
        MediaType mt = MediaType.parse(contentType);
        this.type = mt.type();
        this.subtype = mt.subtype();
        this.charset = mt.charset().or(StandardCharsets.UTF_8);
    }

    @Nonnull
    public String getContentType() {
        return contentType;
    }

    @Nonnull
    public String getType() {
        return type;
    }

    @Nonnull
    public String getSubtype() {
        return subtype;
    }

    @Nonnull
    public Charset getCharset() {
        return charset;
    }

    protected ObjectSerializer createObjectSerializer() {
        if ("json".equalsIgnoreCase(subtype)) {
            if (isJacksonEnabled()) {
                return new JacksonJsonSerializer(this);
            } else if (isGsonEnabled()) {
                return new GsonJsonSerializer(this);
            }
            throw new MockingException("No Json library found.");
        } else if ("xml".equalsIgnoreCase(subtype)) {
            return new JAXBXmlSerializer(this);
        }

        throw new MockingException("No valid Content-Type header found.");
    }

    protected boolean isJacksonEnabled() {
        return Resources.getResource(JACKSON_CLASS) != null;
    }

    protected boolean isGsonEnabled() {
        return Resources.getResource(GSON_CLASS) != null;
    }
}
