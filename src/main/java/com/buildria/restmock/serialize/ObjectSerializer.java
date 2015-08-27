package com.buildria.restmock.serialize;

import com.buildria.restmock.RestMockException;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import java.io.IOException;
import java.util.Objects;

// CHECKSTYLE:OFF
public abstract class ObjectSerializer {
// CHECKSTYLE:ON
    
    private static final String JACKSON_CLASS = "com/fasterxml/jackson/databind/ObjectMapper.class";

    private static final String GSON_CLASS = "com/google/gson/Gson.class";

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


    ObjectSerializer() {
        //
    }

    public static ObjectSerializer create(ObjectSerializeContext ctx) {
        Objects.requireNonNull(ctx);
        String contentType = ctx.getContentType();
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

    private static boolean isJacksonEnabled() {
        return Resources.getResource(JACKSON_CLASS) != null;
    }

    private static boolean isGsonEnabled() {
        return Resources.getResource(GSON_CLASS) != null;
    }

    public abstract String seriaize(ObjectSerializeContext ctx) throws IOException;

}
