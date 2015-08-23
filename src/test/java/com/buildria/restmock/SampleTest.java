package com.buildria.restmock;

import com.buildria.restmock.http.HttpStatus;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.net.MediaType;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.builder.stub.RequestSpec.when;

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
                uri(Matchers.containsString("/api/p")).
        then().
                status(HttpStatus.OK).
                contentType(MediaType.JSON_UTF_8);

       when(server).
                uri("/api/q").
        then().
                status(403).
                contentType("application/xml");

        Thread.sleep(1000 * 60);
    }
}
