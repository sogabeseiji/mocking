package com.buildria.restmock.serialize;

import java.io.IOException;

public interface ObjectSerializer {

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

    String seriaize(ObjectSerializeContext ctx) throws IOException;

}
