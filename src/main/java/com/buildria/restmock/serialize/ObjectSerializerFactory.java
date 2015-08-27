package com.buildria.restmock.serialize;

import java.util.Objects;

public class ObjectSerializerFactory {

    private ObjectSerializerFactory() {
        super();
    }

    public static ObjectSerializer create(ObjectSerializerContext ctx) {
        Objects.requireNonNull(ctx);
        return ctx.createStrategy();
    }

}
