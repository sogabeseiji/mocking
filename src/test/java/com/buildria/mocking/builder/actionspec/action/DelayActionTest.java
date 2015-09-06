package com.buildria.mocking.builder.actionspec.action;

import com.buildria.mocking.TestNameRule;
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

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.junit.Assert.assertThat;

public class DelayActionTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private DelayAction target;

    @Test(expected = NullPointerException.class)
    public void testConstructorPathIsNull() throws Exception {
        Matcher<?> path = null;
        long wait = 1000;
        target = new DelayAction(path, wait);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWaitNegative() {
        Matcher<?> path = equalTo("/api/p");
        long wait = -100;
        target = new DelayAction(path, wait);
    }

    @Test
    public void testApply() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        long wait = 500;
        target = new DelayAction(path, wait);

        HttpRequest req
                = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res
                = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);

        long start = System.currentTimeMillis();
        target.apply(req, res);
        long end = System.currentTimeMillis();

        long actual = end - start;
        assertThat(actual, greaterThanOrEqualTo((long) (wait * 0.9)));
        assertThat(actual, lessThanOrEqualTo((long) (wait * 1.1)));
    }
}
