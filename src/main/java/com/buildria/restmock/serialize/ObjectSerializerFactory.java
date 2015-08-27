package com.buildria.restmock.serialize;

import java.util.Objects;

public class ObjectSerializerFactory {

    private ObjectSerializerFactory() {
        super();
    }

    public static ObjectSerializer create(ObjectSerializeContext ctx) {
        Objects.requireNonNull(ctx);
        return ctx.createStrategy();
    }

}
