package com.buildria.restmock.serialize;

public class ObjectSerializerFactory {

    public static ObjectSerializer create(ObjectSerializeContext ctx) {
        return ctx.createStrategy();
    }

}
