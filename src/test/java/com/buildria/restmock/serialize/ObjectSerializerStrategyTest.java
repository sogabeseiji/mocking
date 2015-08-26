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
 * Test for ObjectSerializerStrategy.
 *
 * @author Seiji Sogabe
 */
public class ObjectSerializerStrategyTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = NullPointerException.class)
    public void testCreateObjectSerializer() {
        ObjectSerializerStrategy.createObjectSerializer(null);
    }

    @Test(expected = RestMockException.class)
    public void testCreateObjectSerializerInvalidContentType() {
        Person person = new Person("Bob", 20);
        ObjectSerializeContext ctx = new ObjectSerializeContext(person, MediaType.JPEG.toString());
        ObjectSerializerStrategy.createObjectSerializer(ctx);
    }

    @Test
    public void testCreateObjectSerializerXml() {
        Person person = new Person("Bob", 20);
        ObjectSerializeContext ctx = new ObjectSerializeContext(person, MediaType.XML_UTF_8.toString());
        ObjectSerializer os = ObjectSerializerStrategy.createObjectSerializer(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(JAXBXmlSerializer.class));
    }

}
