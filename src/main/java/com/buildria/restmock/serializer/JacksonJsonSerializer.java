package com.buildria.restmock.serializer;

import com.fasterxml.jackson.core.JsonEncoding;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class JacksonJsonSerializer extends ObjectSerializer {

    public JacksonJsonSerializer(ObjectSerializerContext ctx) {
        super(ctx);
    }

    @Override
    public String serialize() throws IOException {
        Object obj = getCtx().getObjectToSerialize();
        ObjectMapper mapper = new ObjectMapper();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            JsonGenerator g = new JsonFactory().createGenerator(
                    out, JsonEncoding.UTF8);
            mapper.writeValue(g, obj);
            return out.toString("UTF-8");
        }
    }

}
