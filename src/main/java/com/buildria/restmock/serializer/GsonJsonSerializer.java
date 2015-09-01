package com.buildria.restmock.serializer;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;

public class GsonJsonSerializer extends ObjectSerializer {

    public GsonJsonSerializer(ObjectSerializerContext ctx) {
        super(ctx);
    }

    @Override
    public String serialize() throws IOException {
        Object obj = getCtx().getObjectToSerialize();
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(obj);
    }

}
