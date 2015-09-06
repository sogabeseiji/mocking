package com.buildria.mocking.serializer;

import java.io.IOException;
import java.util.Objects;
import javax.annotation.Nonnull;

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

    public abstract byte[] serialize(@Nonnull Object obj) throws IOException;

}
