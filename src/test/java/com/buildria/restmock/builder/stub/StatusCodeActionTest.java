package com.buildria.restmock.builder.stub;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.stub.Action.StatusCodeAction;
import com.google.common.base.MoreObjects;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class StatusCodeActionTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private StatusCodeAction target;

    @After
    public void tearDown() {
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorPathNull() throws Exception {
        Matcher<?> path = null;
        int code = 200;
        Action action = new StatusCodeAction(path, code);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyResponseNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        int code = 200;

        StatusCodeAction action = new StatusCodeAction(path, code);
        action.apply(null, null);
    }

    @Test
    public void testApplyResponse() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        int code = 404;

        StatusCodeAction action = new StatusCodeAction(path, code);
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

        StatusCodeAction action = new StatusCodeAction(path, code);

        MoreObjects.ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("path"));
        assertThat(answer.toString(), containsString("code"));
    }

}
