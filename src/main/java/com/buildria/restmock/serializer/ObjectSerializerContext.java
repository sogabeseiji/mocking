package com.buildria.restmock.serializer;

import com.buildria.restmock.RestMockException;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import java.util.Objects;

public class ObjectSerializerContext {

    private static final String JACKSON_CLASS
            = "com/fasterxml/jackson/databind/ObjectMapper.class";

    private static final String GSON_CLASS = "com/google/gson/Gson.class";

    private final String contentType;

    public ObjectSerializerContext(String contentType) {
        this.contentType = Objects.requireNonNull(contentType);
    }

    public String getContentType() {
        return contentType;
    }

    protected ObjectSerializer createObjectSerializer() {
        MediaType type = MediaType.parse(contentType);

        if ("json".equalsIgnoreCase(type.subtype())) {
            if (isJacksonEnabled()) {
                return new JacksonJsonSerializer(this);
            } else if (isGsonEnabled()) {
                return new GsonJsonSerializer(this);
            }
            throw new RestMockException("No Json library found.");
        } else if ("xml".equalsIgnoreCase(type.subtype())) {
            return new JAXBXmlSerializer(this);
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
