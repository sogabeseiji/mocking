package com.buildria.restmock;

import com.buildria.restmock.http.HttpStatus;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.net.MediaType;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.builder.stub.RequestSpec.when;
import static com.jayway.restassured.RestAssured.given;

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
        when(server).
                uri("/api/p").
                then().
                status(HttpStatus.SC_200_OK).
                contentType(MediaType.JSON_UTF_8);

        given().
                log().all().
                accept(ContentType.JSON).
        when().
                get("/api/p").
        then().
                log().all().
                statusCode(200).
                contentType(ContentType.JSON);

    }
}
