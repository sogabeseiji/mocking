package com.buildria.restmock.builder.stub;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.stub.Action.RawBodyAction;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class RawBodyActionTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private RawBodyAction target;

    @Test(expected = NullPointerException.class)
    public void testConstructorServerNull() throws Exception {
        StubHttpServer server = null;
        Matcher<?> uri = equalTo("/api/p");
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(server, uri, content);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorUriNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = null;
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(server, uri, content);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorContentNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        byte[] content = null;

        Action action = new RawBodyAction(server, uri, content);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyResponseNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(server, uri, content);
        action.apply(null);
    }

    @Test
    public void testApplyResponse() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(server, uri, content);
        HttpResponse in = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(in);

        assertThat(out, notNullValue());
        assertThat(out.headers().get("Content-Length"), is("7"));

        assertThat(out, instanceOf(DefaultFullHttpResponse.class));
        DefaultFullHttpResponse response = (DefaultFullHttpResponse) out;
        ByteBuf buf = response.content();

        byte[] actual = new byte[buf.readableBytes()];
        buf.readBytes(actual);
        assertThat(actual, is(content));
    }

    @Test
    public void testObjects() {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(server, uri, content);

        MoreObjects.ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("uri"));
        assertThat(answer.toString(), containsString("content"));
    }
}
