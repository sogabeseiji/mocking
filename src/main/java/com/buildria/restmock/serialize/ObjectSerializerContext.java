package com.buildria.restmock.serialize;

import com.buildria.restmock.RestMockException;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import java.util.Objects;

public class ObjectSerializerContext {

    // JSON
    /**
     * ObjectSerializer for Gson.
     */
    public static final ObjectSerializer GSON = new GsonJsonSerializer();

    /**
     * ObjectSerializer for Jackson.
     */
    public static final ObjectSerializer JACKSON = new JacksonJsonSerializer();

    // XML
    /**
     * ObjectSerializer for JAXB.
     */
    public static final ObjectSerializer JAXB = new JAXBXmlSerializer();

    private static final String JACKSON_CLASS = "com/fasterxml/jackson/databind/ObjectMapper.class";

    private static final String GSON_CLASS = "com/google/gson/Gson.class";

    private final Object objectToSerialize;

    private final String contentType;

    public ObjectSerializerContext(Object target, String contentType) {
        this.objectToSerialize = Objects.requireNonNull(target);
        this.contentType = Objects.requireNonNull(contentType);
    }

    public Object getObjectToSerialize() {
        return objectToSerialize;
    }

    public String getContentType() {
        return contentType;
    }

    protected ObjectSerializer createStrategy() {
        MediaType type = MediaType.parse(contentType);

        if ("json".equalsIgnoreCase(type.subtype())) {
            if (isJacksonEnabled()) {
                return JACKSON;
            } else if (isGsonEnabled()) {
                return GSON;
            }
            throw new RestMockException("No Json library found.");
        } else if ("xml".equalsIgnoreCase(type.subtype())) {
            return JAXB;
        }

        throw new RestMockException("No valid Content-Type header found.");
    }

    protected boolean isJacksonEnabled() {
        return Resources.getResource(JACKSON_CLASS) != null;
    }

    protected boolean isGsonEnabled() {
        return Resources.getResource(GSON_CLASS) != null;
    }
}
