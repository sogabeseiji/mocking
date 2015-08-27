package com.buildria.restmock.builder.stub;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.stub.Action.HeaderAction;
import com.buildria.restmock.stub.StubHttpServer;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class HeaderActionTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private HeaderAction target;

    @After
    public void tearDown() {
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorServerNull() throws Exception {
        StubHttpServer server = null;
        Matcher<?> uri = equalTo("/api/p");
        String header = "Content-Type";
        String value = "application/xml";
        Action action = new HeaderAction(server, uri, header, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorUriNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = null;
        String header = "Content-Type";
        String value = "application/xml";
        Action action = new HeaderAction(server, uri, header, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorHeaderNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        String header = null;
        String value = "application/xml";
        Action action = new HeaderAction(server, uri, header, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValueNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        String header = "Content-Type";
        String value = null;
        Action action = new HeaderAction(server, uri, header, value);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyResponseNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        String header = "Content-Type";
        String value = "application/xml";

        Action action = new HeaderAction(server, uri, header, value);
        action.apply(null);
    }

    @Test
    public void testApplyResponse() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        String header = "Content-Type";
        String value = "application/xml";

        Action action = new HeaderAction(server, uri, header, value);
        HttpResponse in = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(in);

        assertThat(out, notNullValue());
        assertThat(out.headers().get("Content-Type"), is("application/xml"));
    }

}