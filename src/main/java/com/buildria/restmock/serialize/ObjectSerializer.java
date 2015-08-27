package com.buildria.restmock.serialize;

import java.io.IOException;

public interface ObjectSerializer {

     // JSON
    /**
     * ObjectSerializer for Gson.
     */
    ObjectSerializer GSON = new GsonJsonSerializer();

    /**
     * ObjectSerializer for Jackson.
     */
    ObjectSerializer JACKSON = new JacksonJsonSerializer();

    // XML
    /**
     * ObjectSerializer for JAXB.
     */
    ObjectSerializer JAXB = new JAXBXmlSerializer();

    String serialize(ObjectSerializerContext ctx) throws IOException;

}
