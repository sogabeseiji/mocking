package com.buildria.restmock.serializer;

import java.io.IOException;
import java.util.Objects;

public abstract class ObjectSerializer {

    private final ObjectSerializerContext ctx;

    public ObjectSerializer(ObjectSerializerContext ctx) {
        Objects.requireNonNull(ctx);
        this.ctx = ctx;
    }

    public ObjectSerializerContext getCtx() {
        return ctx;
    }

    public abstract String serialize() throws IOException;

}
