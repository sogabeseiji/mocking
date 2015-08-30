package com.buildria.restmock;

import com.buildria.restmock.stub.StubHttpServer;
import com.jayway.restassured.RestAssured;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.builder.stub.RequestSpec.when;
import static com.buildria.restmock.builder.verify.MethodSpec.verify;
import static com.buildria.restmock.http.RMHttpStatus.SC_200_OK;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SampleCodeTest {

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
    public void testReadMeSampleCode() {
        Person person = new Person("Bob", 20);

        // Restmock
        when(server).
                path("/api/p").
        then().
                statusCode(SC_200_OK).
                contentType("application/json").
                body(person);

        // Rest-assured
        given().
                log().all().
                accept("application/json").
        when().
                get("/api/p").
        then().
                log().all().
                statusCode(SC_200_OK).
                contentType("application/json").
                body("name", is("Bob")).
                body("old", is(20));

        // Restmock
        verify(server).
                get("/api/p").
                accept("application/json");
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
}
