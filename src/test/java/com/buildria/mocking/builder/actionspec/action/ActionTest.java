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

import com.buildria.mocking.Mocking;
import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.stub.StubHttpServer;
import com.google.common.base.MoreObjects.ToStringHelper;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.mocking.http.MockingHttpHeaders.ACCEPT;
import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for Action.
 *
 * @author Seiji Sogabe
 */
public class ActionTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = NullPointerException.class)
    public void testConstructorPathNull() throws Exception {
        Matcher<?> path = null;
        Action action = new ActionImpl(path);
    }

    @Test
    public void testIsApplicableTrue() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        Action action = new ActionImpl(path);

        boolean answer = action.isApplicable("/api/p");
        assertThat(answer, is(true));
    }

    @Test
    public void testIsApplicableFalse() throws Exception {
        Matcher<?> path = Matchers.startsWith("/api/p");
        Action action = new ActionImpl(path);

        boolean answer = action.isApplicable("/api/q");
        assertThat(answer, is(false));
    }

    @Test
    public void testGetHeader() {
        StubHttpServer server = new StubHttpServer(new Mocking());
        Matcher<?> path = equalTo("/api/p");

        server.addAction(new StatusCodeAction(path, 200));
        server.addAction(new HeaderAction(equalTo("/api/q"), CONTENT_TYPE, "application/json"));
        server.addAction(new HeaderAction(path, CONTENT_TYPE, "application/xml"));

        Action action = new ActionImpl(path);

        HeaderAction contentType = action.getHeader("/api/p", CONTENT_TYPE, server.getActions());
        assertThat(contentType, notNullValue());
        assertThat(contentType.getValue(), is("application/xml"));
    }

    @Test
    public void testGetHeaderCotentTypeNone() {
        StubHttpServer server = new StubHttpServer(new Mocking());
        Matcher<?> path = equalTo("/api/p");

        server.addAction(new HeaderAction(path, ACCEPT, "application/xml"));

        Action action = new ActionImpl(path);

        HeaderAction contentType = action.getHeader("/api/p", CONTENT_TYPE, server.getActions());
        assertThat(contentType, nullValue());
    }

    @Test
    public void testObjects() {
        Matcher<?> path = Matchers.startsWith("/api/p");
        Action action = new ActionImpl(path);

        ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("path"));
    }

    @Test
    public void testToString() {
        Matcher<?> path = Matchers.startsWith("/api/p");
        Action action = new ActionImpl(path);

        String answer = action.toString();
        assertThat(answer, notNullValue());
        assertThat(answer, containsString("path"));
    }

    private static class ActionImpl extends Action {

        public ActionImpl(Matcher<?> path) {
            super(path);
        }

        @Override
        public HttpResponse apply(HttpRequest req, HttpResponse res) {
            return res;
        }

    }
}
