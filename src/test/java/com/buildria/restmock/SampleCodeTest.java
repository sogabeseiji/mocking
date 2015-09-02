package com.buildria.restmock;

import com.jayway.restassured.RestAssured;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.builder.action.RequestActionSpec.when;
import static com.buildria.restmock.builder.rule.MethodRuleSpec.post;
import static com.buildria.restmock.http.RMHttpStatus.SC_200_OK;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SampleCodeTest {

    private static final int PORT = 8888;

    @Rule
    public RestMock restmock = new RestMock(PORT);

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        RestAssured.port = PORT;
    }

    @Test
    public void testReadMeSampleCode() {
        Person person = new Person("Bob", 20);

        // RestMock
        restmock.$(
                    when("/api/p").
                    then().
                        statusCode(SC_200_OK).
                        contentType("application/json").
                        body(person)
        );

        // Rest-assured
        given().
                log().all().
                accept("application/json").
                contentType("application/json").
                body(person).
        when().
                post("/api/p").
        then().
                log().all().
                statusCode(SC_200_OK).
                contentType("application/json").
                body("name", is("Bob")).
                body("old", is(20));

        // RestMock
        restmock.$(
                    post("/api/p").
                    accept("application/json").
                    contentType("application/json; charset=ISO-8859-1").
                    body("name", is("Bob")).
                    body("old", is(20))
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
}
