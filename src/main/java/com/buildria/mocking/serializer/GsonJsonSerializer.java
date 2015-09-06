package com.buildria.mocking.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Objects;
import javax.annotation.Nonnull;

public class GsonJsonSerializer extends ObjectSerializer {

    public GsonJsonSerializer(ObjectSerializerContext ctx) {
        super(ctx);
    }

    @Override
    public byte[] serialize(@Nonnull Object obj) throws IOException {
        Objects.requireNonNull(obj);

        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        Gson gson = builder.create();

        Charset charset = getCtx().getCharset();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try (OutputStreamWriter osw = new OutputStreamWriter(baos, charset)) {
            gson.toJson(obj, osw);
        }
       return baos.toByteArray();
    }

}
