/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
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
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThanOrEqualTo;
import static org.hamcrest.Matchers.notNullValue;
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

    @Test
    public void testObjects() {
        Matcher<?> path = equalTo("/api/p");
        long wait = 500;

        Action action = new DelayAction(path, wait);

        MoreObjects.ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("path"));
        assertThat(answer.toString(), containsString("wait"));
    }
}
