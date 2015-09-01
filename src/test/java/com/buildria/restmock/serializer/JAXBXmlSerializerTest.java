package com.buildria.restmock.serializer;

import com.buildria.restmock.TestNameRule;
import com.jayway.restassured.http.ContentType;
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
                = new ObjectSerializerContext(ContentType.XML.name());
        target = new JAXBXmlSerializer(ctx);

        String xml = target.serialize(person);
    }

    @Test
    public void testSerialize() throws Exception {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(ContentType.XML.name());
        target = new JAXBXmlSerializer(ctx);

        String xml = target.serialize(person);

        assertThat(xml, notNullValue());
        XmlPath xp = new XmlPath(xml);
        assertThat(xp.getString("person.name"), is("Bob"));
        assertThat(xp.getInt("person.old"), is(20));
    }

    @Test(expected = IOException.class)
    public void testSerializeNoAnnotation() throws Exception {
        Animal animal = new Animal("Pooh");
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(ContentType.XML.name());
        target = new JAXBXmlSerializer(ctx);

        String xml = target.serialize(animal);
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
