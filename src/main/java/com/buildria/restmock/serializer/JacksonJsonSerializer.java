package com.buildria.restmock.serializer;

import com.buildria.restmock.RestMockException;
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
        throw new RestMockException("No charset found. " + charset.toString());
    }

}
