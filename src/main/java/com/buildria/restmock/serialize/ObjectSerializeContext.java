package com.buildria.restmock.serialize;

import java.util.Objects;

public class ObjectSerializeContext {

    private final Object objectToSerialize;

    private final String contentType;

    public ObjectSerializeContext(Object target, String contentType) {
        this.objectToSerialize = Objects.requireNonNull(target);
        this.contentType = Objects.requireNonNull(contentType);
    }

    public Object getObjectToSerialize() {
        return objectToSerialize;
    }

    public String getContentType() {
        return contentType;
    }

}
