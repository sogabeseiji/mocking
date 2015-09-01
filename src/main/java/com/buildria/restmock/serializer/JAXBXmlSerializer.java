package com.buildria.restmock.serializer;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBXmlSerializer extends ObjectSerializer {

    public JAXBXmlSerializer(ObjectSerializerContext ctx) {
        super(ctx);
    }

    @Override
    public String serialize() throws IOException {
        Object obj = getCtx().getObjectToSerialize();
        try {
            JAXBContext contextObj = JAXBContext.newInstance(obj.getClass());
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(
                    Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            StringWriter sw = new StringWriter();
            marshallerObj.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            throw new IOException(ex);
        }

    }

}
