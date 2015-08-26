package com.buildria.restmock.serialize;

import java.io.IOException;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBXmlSerializer implements ObjectSerializer {

    @Override
    public String seriaize(ObjectSerializeContext ctx) throws IOException {
        Object obj = ctx.getObjectToSerialize();

        JAXBContext contextObj;
        try {
            contextObj = JAXBContext.newInstance(obj.getClass());
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
