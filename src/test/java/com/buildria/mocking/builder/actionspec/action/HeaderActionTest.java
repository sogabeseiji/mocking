package com.buildria.mocking.builder.actionspec.action;

import com.buildria.mocking.TestNameRule;
import com.google.common.base.MoreObjects;
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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class HeaderActionTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private HeaderAction target;

    @Test(expected = NullPointerException.class)
    public void testConstructorPathNull() throws Exception {
        Matcher<?> path = null;
        String header = "Content-Type";
        String value = "application/xml";
        Action action = new HeaderAction(path, header, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorHeaderNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        String header = null;
        String value = "application/xml";
        Action action = new HeaderAction(path, header, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValueNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        String header = "Content-Type";
        String value = null;
        Action action = new HeaderAction(path, header, value);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyResponseNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        String header = "Content-Type";
        String value = "application/xml";

        Action action = new HeaderAction(path, header, value);
        action.apply(null, null);
    }

    @Test
    public void testApplyResponse() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        String header = "Content-Type";
        String value = "application/xml";

        Action action = new HeaderAction(path, header, value);
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(req, res);

        assertThat(out, notNullValue());
        assertThat(out.headers().get("Content-Type"), is("application/xml"));
    }

    @Test
    public void testObjects() {
        Matcher<?> path = equalTo("/api/p");
        String header = "Content-Type";
        String value = "application/xml";

        Action action = new HeaderAction(path, header, value);

        MoreObjects.ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("path"));
        assertThat(answer.toString(), containsString("header"));
        assertThat(answer.toString(), containsString("value"));
    }

}
