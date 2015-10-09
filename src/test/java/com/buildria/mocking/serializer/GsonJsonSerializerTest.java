/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buildria.mocking.serializer;

import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.serializer.ObjectSerializerContext.SubType;
import com.jayway.restassured.path.json.JsonPath;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for GsonJsonSerializer.
 *
 * @author Seiji Sogabe
 */
public class GsonJsonSerializerTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private GsonJsonSerializer target;

    @Test(expected = NullPointerException.class)
    public void testConstructorCtxNull() throws Exception {
        target = new GsonJsonSerializer(null);
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeObjectNull() throws Exception {
        Person person = null;
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8);
        target = new GsonJsonSerializer(ctx);

        String json = new String(target.serialize(person), "UTF-8");
    }

    @Test
    public void testSerialize() throws Exception {
        Person person = new Person("Bob", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8);
        target = new GsonJsonSerializer(ctx);

        String json = new String(target.serialize(person), "UTF-8");

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("Bob"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test
    public void testSerializeMultibytes() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8);
        target = new GsonJsonSerializer(ctx);

        String json = new String(target.serialize(person), "UTF-8");

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test
    public void testSerializeUTF16BE() throws Exception {
        Person person = new Person("\u3042\u3044\u3046\u3048\u304a", 20);
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_16BE);
        target = new GsonJsonSerializer(ctx);

        String json = new String(target.serialize(person), "UTF-16BE");

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(js.getInt("old"), is(20));
    }

    @Test(expected = NullPointerException.class)
    public void testDesrializeSrcNull() throws Exception {
        InputStream src = null;
        Class<Person> type = Person.class;

        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8);
        target = new GsonJsonSerializer(ctx);
        Person person = target.deserialize(src, type);
    }

    @Test(expected = NullPointerException.class)
    public void testDesrializeTypeNull() throws Exception {
        InputStream src = new ByteArrayInputStream(
                "{\"name\":\"Bob\",\"old\":20}".getBytes(StandardCharsets.UTF_8));
        Class<Person> type = null;

        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8);
        target = new GsonJsonSerializer(ctx);
        Person person = target.deserialize(src, type);
    }

    @Test
    public void testDesrialize() throws Exception {
        InputStream src = new ByteArrayInputStream(
                "{\"name\":\"\u3042\u3044\u3046\u3048\u304a\",\"old\":20}".getBytes(StandardCharsets.UTF_8));
        Class<Person> type = Person.class;

        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8);
        target = new GsonJsonSerializer(ctx);
        Person person = target.deserialize(src, type);

        assertThat(person.getName(), is("\u3042\u3044\u3046\u3048\u304a"));
        assertThat(person.getOld(), is(20));
    }

}
