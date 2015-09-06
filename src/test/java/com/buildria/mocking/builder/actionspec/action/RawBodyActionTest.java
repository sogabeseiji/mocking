package com.buildria.mocking.builder.actionspec.action;

import com.buildria.mocking.TestNameRule;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.mocking.http.RMHttpHeaders.ACCEPT;
import static com.buildria.mocking.http.RMHttpHeaders.CONTENT_LENGTH;
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
    public void testConstructorPathNull() throws Exception {
        Matcher<?> path = null;
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(path, content);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorContentNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        byte[] content = null;

        Action action = new RawBodyAction(path, content);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyResponseNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(path, content);
        action.apply(null, null);
    }

    @Test
    public void testApplyResponse() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(path, content);
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        res.headers().add(ACCEPT, "application/xml");
        HttpResponse out = action.apply(req, res);

        assertThat(out, notNullValue());
        assertThat(out.headers().get(CONTENT_LENGTH), is("7"));
        assertThat(out.headers().get(ACCEPT), is("application/xml"));

        assertThat(out, instanceOf(DefaultFullHttpResponse.class));
        DefaultFullHttpResponse response = (DefaultFullHttpResponse) out;
        ByteBuf buf = response.content();

        byte[] actual = new byte[buf.readableBytes()];
        buf.readBytes(actual);
        assertThat(actual, is(content));
    }

    @Test
    public void testObjects() {
        Matcher<?> path = equalTo("/api/p");
        byte[] content = "content".getBytes();

        Action action = new RawBodyAction(path, content);

        MoreObjects.ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("path"));
        assertThat(answer.toString(), containsString("content"));
    }
}
