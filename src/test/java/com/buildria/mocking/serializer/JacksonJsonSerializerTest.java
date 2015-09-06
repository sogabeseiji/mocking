package com.buildria.mocking.serializer;

import com.buildria.mocking.serializer.ObjectSerializerContext;
import com.buildria.mocking.serializer.JacksonJsonSerializer;
import com.buildria.mocking.RestMockException;
import com.buildria.mocking.TestNameRule;
import com.google.common.net.MediaType;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import java.nio.charset.StandardCharsets;
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
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(ContentType.JSON.toString());
        target = new JacksonJsonSerializer(ctx);

        target.serialize(person);
    }

    @Test
    public void testSerialize() throws Exception {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.JSON_UTF_8.toString());
        target = new JacksonJsonSerializer(ctx);

        String json = new String(target.serialize(person), StandardCharsets.UTF_8);

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("Bob"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test
    public void testSerializeMultibytes() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(MediaType.JSON_UTF_8.toString());
        target = new JacksonJsonSerializer(ctx);

        String json = new String(target.serialize(person), StandardCharsets.UTF_8);

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test
    public void testSerializeUTF16LE() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext("application/json; charset=UTF-16LE");
        target = new JacksonJsonSerializer(ctx);

        String json = new String(target.serialize(person), "UTF-16LE");

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test
    public void testSerializeUTF32LE() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext("application/json; charset=UTF-32LE");
        target = new JacksonJsonSerializer(ctx);

        String json = new String(target.serialize(person), "UTF-32LE");

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test
    public void testSerializeUTF32BE() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext("application/json; charset=UTF-32BE");
        target = new JacksonJsonSerializer(ctx);

        String json = new String(target.serialize(person), "UTF-32BE");

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test(expected = RestMockException.class)
    public void testSerializeNonSupportedCharset() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext("application/json; charset=EUC-JP");
        target = new JacksonJsonSerializer(ctx);

        String json = new String(target.serialize(person), "EUC-JP");
    }

}
