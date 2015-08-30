package com.buildria.restmock.serialize;

import java.util.Objects;

// CHECKSTYLE:OFF
public class ObjectSerializerFactory {
// CHECKSTYLE:ON

    private ObjectSerializerFactory() {
        super();
    }

    public static ObjectSerializer create(ObjectSerializerContext ctx) {
        Objects.requireNonNull(ctx);
        return ctx.createStrategy();
    }

}
