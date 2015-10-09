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
package com.buildria.mocking.serializer;

import com.buildria.mocking.MockingException;
import com.google.common.io.Resources;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.Nonnull;

public class ObjectSerializerContext {

    private static final String JACKSON_CLASS
            = "com/fasterxml/jackson/databind/ObjectMapper.class";

    private static final String GSON_CLASS = "com/google/gson/Gson.class";

    private final SubType type;

    private final Charset charset;

    public enum SubType {

        XML, JSON
    }

    public ObjectSerializerContext(@Nonnull SubType type) {
        this(type, StandardCharsets.UTF_8);
    }

    public ObjectSerializerContext(@Nonnull SubType type, 
            @Nonnull Charset charset) {
        this.type = Objects.requireNonNull(type);
        this.charset = Objects.requireNonNull(charset);
    }

    @Nonnull
    public SubType getSubType() {
        return type;
    }

    @Nonnull
    public Charset getCharset() {
        return charset;
    }

    protected ObjectSerializer createObjectSerializer() {
        if (SubType.JSON.equals(type)) {
            if (isJacksonEnabled()) {
                return new JacksonJsonSerializer(this);
            } else if (isGsonEnabled()) {
                return new GsonJsonSerializer(this);
            }
            throw new MockingException("No Json library found.");
        } else if (SubType.XML.equals(type)) {
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
