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
package com.buildria.mocking;

import com.buildria.mocking.stub.StubHttpServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Resources;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.response.Response;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.buildria.mocking.builder.action.RequestActionSpec.when;
import static com.buildria.mocking.builder.rule.MethodRuleSpec.get;
import static com.buildria.mocking.builder.rule.MethodRuleSpec.put;
import static com.buildria.mocking.http.MockingHttpStatus.SC_200_OK;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class StubSpecTest {

    private StubHttpServer server;

    private static final int PORT = 8888;

    @Rule
    public Mocking mocking = new Mocking(PORT, true);

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        RestAssured.port = mocking.getPort();
    }

    @Test
    public void testOneUri() throws Exception {
        Person p = new Person("hoge", 19);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p);

        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withRawBody(json, Charset.defaultCharset()).
                        withContentType("application/json; charset=UTF-8")
        );

        given().
                accept(ContentType.JSON).
         when().
                get("/api/p").
         then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));

    }

    @Test
    public void testTwoUri() throws Exception {
        Person p = new Person("hoge", 19);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p);

        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withRawBody(json, Charset.defaultCharset()).
                        withHeader("X-header", "restmock1").
                        withContentType("application/json; charset=UTF-8")
        );

        mocking.$(
                when("/api/q").
                then().
                        withStatusCode(SC_200_OK).
                        withRawBody(json).
                        withHeader("X-header", "restmock2").
                        withContentType("application/json; charset=UTF-8")
        );

        given().
                accept(ContentType.JSON).
        when().
                post("/api/p").
        then().
                statusCode(SC_200_OK).
                contentType(ContentType.JSON).
                header("X-header", "restmock1").
                body("name", is("hoge")).
                body("old", is(19));

        given().
                log().all().
                accept(ContentType.JSON).
        when().
                get("/api/q").
        then().
                statusCode(SC_200_OK).
                contentType(ContentType.JSON).
                header("X-header", "restmock2").
                body("name", is("hoge")).
                body("old", is(19));
    }

    @Test
    public void testByteBody() throws Exception {
        Person p = new Person("hoge", 19);
        ObjectMapper mapper = new ObjectMapper();
        byte[] json = mapper.writeValueAsString(p).getBytes(StandardCharsets.UTF_8);

        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withRawBody(json).
                        withContentType("application/json; charset=UTF-8")
        );

        given().
                accept(ContentType.JSON).
        when().
                get("/api/p").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));
    }

    @Test
    public void testURIBody() throws Exception {
        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withRawBody(Resources.getResource("com/buildria/mocking/person.json")).
                        withContentType("application/json; charset=UTF-8")
        );

        given().
                accept(ContentType.JSON).
        when().
                get("/api/p").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));
    }

    @Test
    public void testStreamBody() throws Exception {
        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withRawBody(Resources.getResource("com/buildria/mocking/person.json").openStream()).
                        withContentType("application/json; charset=UTF-8")
        );

        given().
                accept(ContentType.JSON).
        when().
                get("/api/p").
        then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));

    }

    @Test
    public void testRequestBodyMultibytes() throws Exception {
        Person p = new Person("\u3042\u3044\u3046\u3048\u304a", 19);

        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withBody(p).
                        withContentType("application/xml; charset=UTF-8")
        );

        Response r =
                given().
                accept(ContentType.XML).
                contentType(ContentType.XML).
                body(p).
        when().
                put("/api/p").
        then().
                statusCode(200).
                contentType(ContentType.XML).
                body("person.name", is("\u3042\u3044\u3046\u3048\u304a")).
                body("person.old", is("19")).
        extract().
                response();

        LOG.debug("### body: {}", r.getBody().asString());

        mocking.$(
                put("/api/p").
                then().
                        withAccept(containsString("application/xml")).
                        withContentType(containsString("application/xml")).
                        withBody("person.name", is("\u3042\u3044\u3046\u3048\u304a")).
                        withBody("person.old", is("19"))

        );
    }

    @Test
    public void testRequestXmlBody() throws Exception {
        Person p = new Person("あいうえお", 19);

        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withBody(p).
                        withContentType("application/xml; charset=UTF-8")
        );

        given().
                accept(ContentType.XML).
        when().
                put("/api/p").
        then().
                statusCode(200).
                contentType(ContentType.XML).
                body("persson.name", is("あいうえお")).
                body("person.old", is("19"));
    }

    @Test
    public void testQueryParam() throws Exception {
        Person p = new Person("hoge", 19);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p);

        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withRawBody(json, Charset.defaultCharset()).
                        withContentType("application/json; charset=UTF-8")
        );

        given().
                accept(ContentType.JSON).
                queryParam("name", "value 1").
         when().
                get("/api/p").
         then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));

        mocking.$(
                get("/api/p").
                then().
                        withAccept(containsString("application/json")).
                        withQueryParam("name", "value 1")
        );
    }

    @Test
    public void testQueryParam2() throws Exception {
        Person p = new Person("hoge", 19);

        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_200_OK).
                        withBody(p).
                        withContentType("application/json; charset=UTF-8")
        );

        given().
                accept(ContentType.JSON).
                queryParam("name", "value 1").
         when().
                get("/api/p").
         then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));

        mocking.$(
                get("/api/p").
                then().
                        withAccept(containsString("application/json")).
                        withQueryParam("name", "value 1")
        );
    }

    @Test
    public void testQueryParams() throws Exception {
        Person p = new Person("hoge", 19);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p);

       mocking.$(
               when("/api/p").
               then().
                       withStatusCode(SC_200_OK).
                       withRawBody(json, Charset.defaultCharset()).
                       withContentType("application/json; charset=UTF-8")
       );

        given().
                accept(ContentType.JSON).
                queryParam("name", "value 1").
                queryParam("name", "value 2").
         when().
                get("/api/p").
         then().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));

        mocking.$(
                get("/api/p").
                then().
                        withAccept(containsString("application/json")).
                        withQueryParam("name", "value 1").
                        withQueryParam("name", "value 2")
        );
    }

    @XmlRootElement(name = "person")
    @XmlType
    private static class Person {

        private String name;

        private int old;

        public Person() {
            //
        }

        public Person(String name, int old) {
            this.name = name;
            this.old = old;
        }

        @XmlElement
        public String getName() {
            return name;
        }

        @XmlElement
        public int getOld() {
            return old;
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(StubSpecTest.class);
}
