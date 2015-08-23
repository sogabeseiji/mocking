package com.buildria.restmock;

import com.buildria.restmock.http.HttpStatus;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.net.MediaType;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.builder.stub.RequestSpec.when;
import static org.hamcrest.Matchers.containsString;

/**
 *
 * @author sogabe
 */
public class SampleTest {

    private StubHttpServer server;

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Before
    public void setUp() throws Exception {
        server = new StubHttpServer(8080).run();
    }

    @After
    public void tearDown() throws Exception {
        server.stop();
    }

    @Test
    public void testSample() throws Exception {
        when(server).
                uri(containsString("/api/p")).
        then().
                status(HttpStatus.SC_OK).
                contentType(MediaType.JSON_UTF_8);

       when(server).
                uri("/api/q").
        then().
                status(HttpStatus.SC_NOT_FOUND).
                contentType("application/xml");

        Thread.sleep(1000 * 60);
    }
}
