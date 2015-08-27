package com.buildria.restmock.serialize;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.util.Objects;

public class GsonJsonSerializer extends ObjectSerializer {

    GsonJsonSerializer() {
        //
    }

    @Override
    public String serialize(ObjectSerializeContext ctx) throws IOException {
        Objects.requireNonNull(ctx);
        Object obj = ctx.getObjectToSerialize();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(obj);
    }

}
