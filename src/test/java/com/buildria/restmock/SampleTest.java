package com.buildria.restmock;

import com.buildria.restmock.http.HttpStatus;
import com.buildria.restmock.stub.StubHttpServer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.net.MediaType;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import java.nio.charset.Charset;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.builder.stub.RequestSpec.when;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

/**
 *
 * @author sogabe
 */
public class SampleTest {

    private StubHttpServer server;

    private static final int PORT = 8888;

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        // ポート番号
        RestAssured.port = PORT;
        server = new StubHttpServer(PORT).run();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testSample() throws Exception {
        Person p = new Person("hoge", 19);
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(p);

        when(server).
                uri("/api/p").
                then().
                status(HttpStatus.SC_200_OK).
                body(json, Charset.defaultCharset()).
                contentType(MediaType.JSON_UTF_8);

        given().
                log().all().
                accept(ContentType.JSON).
        when().
                get("/api/p").
        then().
                log().all().
                statusCode(200).
                body("name", is("hoge")).
                body("old", is(19));

    }


    private static class Person {

        private final String name;

        private final int old;

        public Person(String name, int old) {
            this.name = name;
            this.old = old;
        }

        public String getName() {
            return name;
        }

        public int getOld() {
            return old;
        }

    }
}
