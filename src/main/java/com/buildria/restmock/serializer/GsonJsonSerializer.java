package com.buildria.restmock.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nonnull;

public class GsonJsonSerializer extends ObjectSerializer {

    public GsonJsonSerializer(ObjectSerializerContext ctx) {
        super(ctx);
    }

    // TODO charrset
    @Override
    public String serialize(@Nonnull Object obj) throws IOException {
        Objects.requireNonNull(obj);
        GsonBuilder builder = new GsonBuilder();
        builder.disableHtmlEscaping();
        Gson gson = builder.create();
        return gson.toJson(obj);
    }

}
