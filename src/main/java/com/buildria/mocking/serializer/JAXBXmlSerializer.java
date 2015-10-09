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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

public class JAXBXmlSerializer implements ObjectSerializer {

    private final ObjectSerializerContext ctx;

    public JAXBXmlSerializer(ObjectSerializerContext ctx) {
        this.ctx = Objects.requireNonNull(ctx);
    }

    @Override
    public byte[] serialize(@Nonnull Object obj) throws IOException {
        Objects.requireNonNull(obj);
        try {
            JAXBContext context = JAXBContext.newInstance(obj.getClass());
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(
                    Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            marshaller.setProperty(
                    Marshaller.JAXB_ENCODING, ctx.getCharset().toString());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshaller.marshal(obj, baos);
            return baos.toByteArray();
        } catch (JAXBException ex) {
            throw new IOException(ex);
        }

    }

    @Override
    public <T> T deserialize(@Nonnull InputStream src, @Nonnull Class<T> type)
            throws IOException {
        Objects.requireNonNull(src);
        Objects.requireNonNull(type);
        try {
            JAXBContext context = JAXBContext.newInstance(type);
            Unmarshaller unmarshaller = context.createUnmarshaller();
            try (InputStreamReader is 
                    = new InputStreamReader(src, ctx.getCharset())) {
                Object obj = unmarshaller.unmarshal(is);
                return type.cast(obj);
            }
        } catch (JAXBException ex) {
            throw new IOException(ex);
        }
    }

}
