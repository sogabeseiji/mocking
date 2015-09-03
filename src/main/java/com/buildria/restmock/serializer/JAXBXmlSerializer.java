package com.buildria.restmock.serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
    public byte[] serialize(@Nonnull Object obj) throws IOException {
        Objects.requireNonNull(obj);
        try {
            JAXBContext contextObj = JAXBContext.newInstance(obj.getClass());
            Marshaller marshallerObj = contextObj.createMarshaller();
            marshallerObj.setProperty(
                    Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.FALSE);
            marshallerObj.setProperty(Marshaller.JAXB_ENCODING, getCtx().getCharset().toString());
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            marshallerObj.marshal(obj, baos);
            return baos.toByteArray();
        } catch (JAXBException ex) {
            throw new IOException(ex);
        }

    }

}
