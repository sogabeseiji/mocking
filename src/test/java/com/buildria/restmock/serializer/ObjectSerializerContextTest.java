package com.buildria.restmock.serializer;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.TestNameRule;
import com.google.common.net.MediaType;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ObjectSerializerContextTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = RestMockException.class)
    public void testCreateInvalidContentType() {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.JPEG.toString());
        ObjectSerializerFactory.create(ctx);
    }

    @Test
    public void testContentTypeWithCharset() {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.APPLICATION_XML_UTF_8.toString());
        assertThat(ctx.getContentType(), is(MediaType.APPLICATION_XML_UTF_8.toString()));
        assertThat(ctx.getType(), is("application"));
        assertThat(ctx.getSubtype(), is("xml"));
        assertThat(ctx.getCharset().name(), is("UTF-8"));
    }

    @Test
    public void testContentTypeWithNoCharset() {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext("application/json");
        assertThat(ctx.getContentType(), is("application/json"));
        assertThat(ctx.getType(), is("application"));
        assertThat(ctx.getSubtype(), is("json"));
        assertThat(ctx.getCharset().name(), is("UTF-8"));
    }

    @Test
    public void testCreateJAXB() {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.APPLICATION_XML_UTF_8.toString());
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(JAXBXmlSerializer.class));
    }

    @Test
    public void testCreateJackson() {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.JSON_UTF_8.toString());
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(JacksonJsonSerializer.class));
    }

    @Test
    public void testCreateGson() {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.JSON_UTF_8.toString()) {
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
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.JSON_UTF_8.toString()) {
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
