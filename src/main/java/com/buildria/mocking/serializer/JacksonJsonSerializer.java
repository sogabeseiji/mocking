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
import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.Nonnull;

public class JacksonJsonSerializer extends ObjectSerializer {

    public JacksonJsonSerializer(ObjectSerializerContext ctx) {
        super(ctx);
    }

    @Override
    public byte[] serialize(@Nonnull Object obj) throws IOException {
        Objects.requireNonNull(obj);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonEncoding encoding = mappingFrom(getCtx().getCharset());
        try (JsonGenerator g = new JsonFactory().createGenerator(out, encoding)) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(g, obj);
        }
        
        return out.toByteArray();
    }

    private JsonEncoding mappingFrom(Charset charset) {
        if (StandardCharsets.UTF_8.equals(charset)) {
            return JsonEncoding.UTF8;
        }
        if (StandardCharsets.UTF_16BE.equals(charset)) {
            return JsonEncoding.UTF16_BE;
        }
        if (StandardCharsets.UTF_16LE.equals(charset)) {
            return JsonEncoding.UTF16_LE;
        }
        if (Charset.forName("UTF-32BE").equals(charset)) {
            return JsonEncoding.UTF32_BE;
        }
        if (Charset.forName("UTF-32LE").equals(charset)) {
            return JsonEncoding.UTF32_LE;
        }
        throw new MockingException("No charset found. " + charset.toString());
    }

}
