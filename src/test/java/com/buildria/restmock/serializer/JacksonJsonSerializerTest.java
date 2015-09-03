package com.buildria.restmock.serializer;

import com.buildria.restmock.TestNameRule;
import com.google.common.net.MediaType;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for JacksonJsonSerializzer.
 *
 * @author Seiji Sogabe
 */
public class JacksonJsonSerializerTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private JacksonJsonSerializer target;

    @Test(expected = NullPointerException.class)
    public void testConstructorCtxNull() throws Exception {
        target = new JacksonJsonSerializer(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeObjectNull() throws Exception {
        Person person = null;
        ObjectSerializerContext ctx =
                new ObjectSerializerContext(ContentType.JSON.toString());
        target = new JacksonJsonSerializer(ctx);

        String json = target.serialize(person);
    }

    @Test
    public void testSerialize() throws Exception {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx =
                new ObjectSerializerContext(MediaType.JSON_UTF_8.toString());
        target = new JacksonJsonSerializer(ctx);

        String json = target.serialize(person);

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("Bob"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test
    public void testSerializeMultibytes() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx =
                new ObjectSerializerContext(MediaType.JSON_UTF_8.toString());
        target = new JacksonJsonSerializer(ctx);

        String json = target.serialize(person);

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(js.getInt("old"), is(20));
    }

}
