package com.buildria.restmock.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBXmlSerializer implements ObjectSerializer {

    JAXBXmlSerializer() {
        //
    }

    @Override
    public String serialize(ObjectSerializerContext ctx) throws IOException {
        Objects.requireNonNull(ctx);
        Object obj = ctx.getObjectToSerialize();
        try {
            JAXBContext contextObj = JAXBContext.newInstance(obj.getClass());
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            StringWriter sw = new StringWriter();
            marshallerObj.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            throw new IOException(ex);
        }

    }

}
