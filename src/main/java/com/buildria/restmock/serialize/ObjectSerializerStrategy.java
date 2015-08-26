package com.buildria.restmock.serialize;

import com.buildria.restmock.RestMockException;
import com.google.common.io.Resources;
import com.google.common.net.MediaType;
import java.util.Objects;

public class ObjectSerializerStrategy {

    private static final String JACKSON_CLASS = "com/fasterxml/jackson/databind/ObjectMapper.class";

    private static final String GSON_CLASS = "com/google/gson/Gson.class";

    private ObjectSerializerStrategy() {
        //
    }

    public static ObjectSerializer createObjectSerializer(ObjectSerializeContext ctx) {
        Objects.requireNonNull(ctx);
        String contentType = ctx.getContentType();
        MediaType type = MediaType.parse(contentType);

        if (type.subtype().equalsIgnoreCase("json")) {
            if (isJacksonEnabled()) {
                return new JacksonJsonSerializer();
            } else if (isGsonEnabled()) {
                return new GsonJsonSerializer();
            }
            throw new RestMockException("No Json library found.");
        } else if (type.subtype().equalsIgnoreCase("xml")) {
            return new JAXBXmlSerializer();
        }

        throw new RestMockException("No valid Content-Type header found.");
    }


    private static boolean isJacksonEnabled() {
        return Resources.getResource(JACKSON_CLASS) != null;
    }

    private static boolean isGsonEnabled() {
        return Resources.getResource(GSON_CLASS) != null;
    }

}
