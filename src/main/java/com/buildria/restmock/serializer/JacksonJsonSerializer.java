package com.buildria.restmock.serializer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Objects;

public class JacksonJsonSerializer implements ObjectSerializer {

    JacksonJsonSerializer() {
        //
    }

    @Override
    public String serialize(ObjectSerializerContext ctx) throws IOException {
        Objects.requireNonNull(ctx);
        Object obj = ctx.getObjectToSerialize();
        ObjectMapper mapper = new ObjectMapper();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JsonGenerator g = new JsonFactory().createGenerator(out, JsonEncoding.UTF8);
            mapper.writeValue(g, obj);
            return out.toString("UTF-8");
        }
    }

}