package com.buildria.restmock.serializer;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

public class JAXBXmlSerializer extends ObjectSerializer {

    public JAXBXmlSerializer(ObjectSerializerContext ctx) {
        super(ctx);
    }

    @Override
    public String serialize(@Nonnull Object obj) throws IOException {
        Objects.requireNonNull(obj);
        try {
            JAXBContext contextObj = JAXBContext.newInstance(obj.getClass());
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(
                    Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            marshallerObj.setProperty(Marshaller.JAXB_ENCODING, getCtx().getCharset().toString());
            StringWriter sw = new StringWriter();
            marshallerObj.marshal(obj, sw);
            return sw.toString();
        } catch (JAXBException ex) {
            throw new IOException(ex);
        }

    }

}
