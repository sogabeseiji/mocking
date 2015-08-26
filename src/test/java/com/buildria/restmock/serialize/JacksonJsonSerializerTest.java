package com.buildria.restmock.serialize;

import com.buildria.restmock.TestNameRule;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.path.json.JsonPath;
import org.junit.Before;
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

    @Before
    public void setUp() {
        target = new JacksonJsonSerializer();
    }

    @Test(expected = NullPointerException.class)
    public void testSerializeCtxNull() throws Exception {
        target.seriaize(null);
    }

    @Test
    public void testSerialize() throws Exception {
        Person person = new Person("Bob", 20);
        ObjectSerializeContext ctx =
                new ObjectSerializeContext(person, ContentType.JSON.name());

        String json = target.seriaize(ctx);

        assertThat(json, notNullValue());
        JsonPath js = new JsonPath(json);
        assertThat(js.getString("name"), is("Bob"));
        assertThat(js.getInt("old"), is(20));
    }
}
