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

import com.jayway.restassured.RestAssured;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.mocking.builder.actionspec.RequestActionSpec.when;
import static com.buildria.mocking.builder.rulespec.MethodRuleSpec.post;
import static com.buildria.mocking.http.MockingHttpStatus.SC_201_CREATED;
import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

public class SampleCodeTest {

    private static final int PORT = 8888;

    @Rule
    public Mocking mocking = new Mocking();

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        mocking.port(PORT).logging(true);
        RestAssured.port = PORT;
    }

    @Test
    public void testReadMeSampleCode() {
        Person person = new Person("Bob", 20);

        // Mocking
        mocking.$(
                when("/api/p").
                then().
                        withStatusCode(SC_201_CREATED).
                        withContentType("application/json; charset=UTF-8").
                        withBody(person)
        );

        // Rest-assured
        given().
                accept("application/json").
                contentType("application/json; charset=UTF-8").
                body(person).
        when().
                post("/api/p").
        then().
                statusCode(SC_201_CREATED).
                contentType("application/json; charset=UTF-8").
                body("name", is("Bob")).
                body("old", is(20));

        // Mocking
        mocking.$(
                post("/api/p").
                        withAccept("application/json").
                        withContentType("application/json; charset=UTF-8").
                        withBody("name", is("Bob")).
                        withBody("old", is(20))
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
