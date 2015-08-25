package com.buildria.restmock.serialize;

import com.google.gson.Gson;
import java.io.IOException;

public class GsonJsonSerializer implements ObjectSerializer {

    @Override
    public String seriaize(ObjectSerializeContext ctx) throws IOException {
        Object obj = ctx.getObjectToSerialize();
        Gson gson = new Gson();
        return gson.toJson(obj);
    }

}
