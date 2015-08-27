package com.buildria.restmock.serialize;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.TestNameRule;
import com.google.common.net.MediaType;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ObjectSerializerContextTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = RestMockException.class)
    public void testCreateInvalidContentType() {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx = new ObjectSerializerContext(person, MediaType.JPEG.toString());
        ObjectSerializerFactory.create(ctx);
    }

    @Test
    public void testCreateJAXB() {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx = new ObjectSerializerContext(person,
                MediaType.XML_UTF_8.toString());
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(JAXBXmlSerializer.class));
    }

    @Test
    public void testCreateJackson() {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx = new ObjectSerializerContext(person,
                MediaType.JSON_UTF_8.toString());
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(JacksonJsonSerializer.class));
    }

    @Test
    public void testCreateGson() {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(person, MediaType.JSON_UTF_8.toString()) {
                    @Override
                    protected boolean isJacksonEnabled() {
                        return false;
                    }
                };
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(GsonJsonSerializer.class));
    }

    @Test(expected = RestMockException.class)
    public void testCreateNoJsonFound() throws Exception {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(person, MediaType.JSON_UTF_8.toString()) {
                    @Override
                    protected boolean isGsonEnabled() {
                        return false;
                    }

                    @Override
                    protected boolean isJacksonEnabled() {
                        return false;
                    }
                };
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
    }
}
