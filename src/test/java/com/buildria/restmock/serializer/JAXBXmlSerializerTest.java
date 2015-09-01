package com.buildria.restmock.serializer;

import com.buildria.restmock.TestNameRule;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.xml.XmlPath;
import java.io.IOException;
import org.junit.Before;
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

    @Before
    public void setUp() {
        target = new JAXBXmlSerializer();
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeCtxNull() throws Exception {
        target.serialize(null);
    }

    @Test
    public void testSerialize() throws Exception {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(person, ContentType.XML.name());

        String xml = target.serialize(ctx);

        assertThat(xml, notNullValue());
        XmlPath xp = new XmlPath(xml);
        assertThat(xp.getString("person.name"), is("Bob"));
        assertThat(xp.getInt("person.old"), is(20));
    }

    @Test(expected = IOException.class)
    public void testSerializeNoAnnotation() throws Exception {
        Animal animal = new Animal("Pooh");
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(animal, ContentType.XML.name());

        String xml = target.serialize(ctx);
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
