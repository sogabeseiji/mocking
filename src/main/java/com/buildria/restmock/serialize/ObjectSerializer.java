package com.buildria.restmock.serialize;

import java.io.IOException;

// CHECKSTYLE:OFF
public abstract class ObjectSerializer {
// CHECKSTYLE:ON

    ObjectSerializer() {
        //
    }

    public abstract String serialize(ObjectSerializeContext ctx) throws IOException;

    public static ObjectSerializer create(ObjectSerializeContext ctx) {
        return ctx.createStrategy();
    }

}
