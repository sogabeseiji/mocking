package com.buildria.restmock.serialize;

public class ObjectSerializeContext {

    private final Object objectToSerialize;

    private final String contentType;

    public ObjectSerializeContext(Object target, String contentType) {
        this.objectToSerialize = target;
        this.contentType = contentType;
    }

    public Object getObjectToSerialize() {
        return objectToSerialize;
    }

    public String getContentType() {
        return contentType;
    }

}
