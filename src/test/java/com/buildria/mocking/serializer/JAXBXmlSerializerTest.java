package com.buildria.mocking.serializer;

import com.buildria.mocking.serializer.ObjectSerializerContext;
import com.buildria.mocking.serializer.JAXBXmlSerializer;
import com.buildria.mocking.TestNameRule;
import com.google.common.net.MediaType;
import com.jayway.restassured.path.xml.XmlPath;
import java.io.IOException;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for JAXBXmlSerializer.
 *
 * @author Seiji Sogabe
 */
public class JAXBXmlSerializerTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private JAXBXmlSerializer target;

    @Test(expected = NullPointerException.class)
    public void testConstructorCtxNull() throws Exception {
        target = new JAXBXmlSerializer(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeObjectNull() throws Exception {
        Person person = null;
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.APPLICATION_XML_UTF_8.toString());
        target = new JAXBXmlSerializer(ctx);

        String xml = new String(target.serialize(person), "UTF-8");
    }

    @Test
    public void testSerialize() throws Exception {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.APPLICATION_XML_UTF_8.toString());
        target = new JAXBXmlSerializer(ctx);

        String xml = new String(target.serialize(person), "UTF-8");

        assertThat(xml, notNullValue());
        XmlPath xp = new XmlPath(xml);
        assertThat(xp.getString("person.name"), is("Bob"));
        assertThat(xp.getInt("person.old"), is(20));
    }

    @Test(expected = IOException.class)
    public void testSerializeNoAnnotation() throws Exception {
        Animal animal = new Animal("Pooh");
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.APPLICATION_XML_UTF_8.toString());
        target = new JAXBXmlSerializer(ctx);

        target.serialize(animal);
    }

    @Test
    public void testSerializeEUCJP() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext("application/xml; charset=EUC-JP");
        target = new JAXBXmlSerializer(ctx);

        String xml = new String(target.serialize(person), "EUC-JP");

        assertThat(xml, notNullValue());
        XmlPath xp = new XmlPath(xml);
        assertThat(xp.getString("person.name"), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(xp.getInt("person.old"), is(20));
    }

    /**
     * No Annotaion.
     */
    private static class Animal {

        private String name;

        public Animal() {
            super();
        }

        public Animal(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

    }
}
