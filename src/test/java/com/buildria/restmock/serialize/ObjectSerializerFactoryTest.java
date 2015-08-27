package com.buildria.restmock.serialize;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.TestNameRule;
import com.google.common.net.MediaType;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for ObjectSerializerFactory.
 *
 * @author Seiji Sogabe
 */
public class ObjectSerializerFactoryTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = NullPointerException.class)
    public void testCreate() {
        ObjectSerializerFactory.create(null);
    }

    @Test(expected = RestMockException.class)
    public void testCreateInvalidContentType() {
        Person person = new Person("Bob", 20);
        ObjectSerializeContext ctx = new ObjectSerializeContext(person, MediaType.JPEG.toString());
        ObjectSerializerFactory.create(ctx);
    }

    @Test
    public void testCreateJAXB() {
        Person person = new Person("Bob", 20);
        ObjectSerializeContext ctx = new ObjectSerializeContext(person,
                MediaType.XML_UTF_8.toString());
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(JAXBXmlSerializer.class));
    }

    @Test
    public void testCreateJackson() {
        Person person = new Person("Bob", 20);
        ObjectSerializeContext ctx = new ObjectSerializeContext(person,
                MediaType.JSON_UTF_8.toString());
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());

        ObjectSe

    }


}