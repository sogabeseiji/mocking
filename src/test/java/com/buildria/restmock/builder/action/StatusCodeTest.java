package com.buildria.restmock.builder.action;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.action.Action.StatusCode;
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

public class StatusCodeTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private StatusCode target;

    @Test(expected = NullPointerException.class)
    public void testConstructorPathNull() throws Exception {
        Matcher<?> path = null;
        int code = 200;
        Action action = new StatusCode(path, code);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyResponseNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        int code = 200;

        StatusCode action = new StatusCode(path, code);
        action.apply(null, null);
    }

    @Test
    public void testApplyResponse() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        int code = 404;

        StatusCode action = new StatusCode(path, code);
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(req, res);

        assertThat(out, notNullValue());
        assertThat(out.getStatus().code(), is(code));
    }

    @Test
    public void testObjects() {
        Matcher<?> path = equalTo("/api/p");
        int code = 404;

        StatusCode action = new StatusCode(path, code);

        MoreObjects.ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("path"));
        assertThat(answer.toString(), containsString("code"));
    }

}
