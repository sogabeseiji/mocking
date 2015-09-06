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

import static com.buildria.mocking.builder.actionspec.RequestActionSpec.when;
import static com.buildria.mocking.builder.rulespec.MethodRuleSpec.get;
import static com.buildria.mocking.builder.rulespec.MethodRuleSpec.put;
import static com.buildria.mocking.http.RMHttpStatus.SC_200_OK;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class StubSpecTest {

    private StubHttpServer server;

    private static final int PORT = 8888;

    @Rule
    public Mocking mocking = new Mocking();

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        // ポート番号
        mocking.port(PORT);
        RestAssured.port = PORT;
    }

    @Test
    public void testOneUri() throws Exception {
        Person p = new Person("hoge", 19);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p);

        mocking.$(
                when("/api/p").
                then().
                    statusCode(SC_200_OK).
                    rawBody(json, Charset.defaultCharset()).
                    contentType("application/json; charset=UTF-8")
        );

        given().
                log().all().
                accept(ContentType.JSON).
         when().
                get("/api/p").
         then().
                log().all().
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
                    statusCode(SC_200_OK).
                    rawBody(json, Charset.defaultCharset()).
                    header("X-header", "restmock1").
                    contentType("application/json; charset=UTF-8")
        );

        mocking.$(
                when("/api/q").
                then().
                    statusCode(SC_200_OK).
                    rawBody(json).
                    header("X-header", "restmock2").
                    contentType("application/json; charset=UTF-8")
        );

        given().
                log().all().
                accept(ContentType.JSON).
        when().
                post("/api/p").
        then().
                log().all().
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
                log().all().
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
                    statusCode(SC_200_OK).
                    rawBody(json).
                    contentType("application/json; charset=UTF-8")
        );

        given().
                log().all().
                accept(ContentType.JSON).
        when().
                get("/api/p").
        then().
                log().all().
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
                    statusCode(SC_200_OK).
                    rawBody(Resources.getResource("com/buildria/mocking/person.json")).
                    contentType("application/json; charset=UTF-8")
        );

        given().
                log().all().
                accept(ContentType.JSON).
        when().
                get("/api/p").
        then().
                log().all().
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
                    statusCode(SC_200_OK).
                    rawBody(Resources.getResource("com/buildria/mocking/person.json").openStream()).
                    contentType("application/json; charset=UTF-8")
        );

        given().
                log().all().
                accept(ContentType.JSON).
        when().
                get("/api/p").
        then().
                log().all().
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
                    statusCode(SC_200_OK).
                    body(p).
                    contentType("application/xml; charset=UTF-8")
        );

        Response r =
                given().
                log().all().
                accept(ContentType.XML).
                contentType(ContentType.XML).
                body(p).
        when().
                put("/api/p").
        then().
                log().all().
                statusCode(200).
                contentType(ContentType.XML).
                body("person.name", is("\u3042\u3044\u3046\u3048\u304a")).
                body("person.old", is("19")).
        extract().
                response();

        LOG.debug("### body: {}", r.getBody().asString());

        mocking.$(
                put("/api/p").
                        accept(containsString("application/xml")).
                        contentType(containsString("pplication/xml")).
                        body("person.name", is("\u3042\u3044\u3046\u3048\u304a")).
                        body("person.old", is("19"))

        );
    }

    @Test
    public void testRequestXmlBody() throws Exception {
        Person p = new Person("あいうえお", 19);

        mocking.$(
                when("/api/p").
                then().
                    statusCode(SC_200_OK).
                    body(p).
                    contentType("application/xml; charset=UTF-8")
        );

        given().
                log().all().
                accept(ContentType.XML).
        when().
                put("/api/p").
        then().
                log().all().
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
                    statusCode(SC_200_OK).
                    rawBody(json, Charset.defaultCharset()).
                    contentType("application/json; charset=UTF-8")
        );

        given().
                log().all().
                accept(ContentType.JSON).
                queryParam("name", "value 1").
         when().
                get("/api/p").
         then().
                log().all().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));

        mocking.$(
                get("/api/p").
                accept(containsString("application/json")).
                queryParam("name", "value 1")
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
                statusCode(SC_200_OK).
                rawBody(json, Charset.defaultCharset()).
                contentType("application/json; charset=UTF-8")
       );

        given().
                log().all().
                accept(ContentType.JSON).
                queryParam("name", "value 1").
                queryParam("name", "value 2").
         when().
                get("/api/p").
         then().
                log().all().
                statusCode(200).
                contentType(ContentType.JSON).
                body("name", is("hoge")).
                body("old", is(19));

        mocking.$(
                get("/api/p").
                accept(containsString("application/json")).
                queryParams("name", "value 1", "value 2")
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
