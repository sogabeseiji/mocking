package com.buildria.restmock.serialize;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Objects;

public class GsonJsonSerializer implements ObjectSerializer {

    GsonJsonSerializer() {
        //
    }

    @Override
    public String seriaize(ObjectSerializeContext ctx) throws IOException {
        Objects.requireNonNull(ctx);
        Object obj = ctx.getObjectToSerialize();
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

}
