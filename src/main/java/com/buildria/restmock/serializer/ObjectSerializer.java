package com.buildria.restmock.serializer;

import java.io.IOException;
import java.util.Objects;

// CHECKSTYLE:OFF
public abstract class ObjectSerializer {
// CHECKSTYLE:ON

    private final ObjectSerializerContext ctx;

    public ObjectSerializer(ObjectSerializerContext ctx) {
        this.ctx = Objects.requireNonNull(ctx);
    }

    public ObjectSerializerContext getCtx() {
        return ctx;
    }

    public abstract String serialize() throws IOException;

}
