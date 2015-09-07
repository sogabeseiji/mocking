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
package com.buildria.mocking.stub;

import com.buildria.mocking.TestNameRule;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import java.util.Arrays;
import org.junit.Rule;
import org.junit.Test;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CallTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = NullPointerException.class)
    public void testFromRequestRequestNull() throws Exception {
        HttpRequest req = null;
        Call.fromRequest(req);
    }

    @Test
    public void testFromRequest() throws Exception {
        DefaultFullHttpRequest req = mock(DefaultFullHttpRequest.class);

        when(req.getUri()).thenReturn("/api/p?name=%E3%81%82");
        when(req.getMethod()).thenReturn(GET);

        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("key", "value1");
        headers.add("key", "value2");
        when(req.headers()).thenReturn(headers);

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte((byte) 0xff);
        when(req.content()).thenReturn(buf);

        Call call = Call.fromRequest(req);

        assertThat(call.getPath(), is("/api/p"));
        assertThat(call.getParameters(), hasEntry("name", Arrays.asList("\u3042")));
        assertThat(call.getMethod(), is(equalToIgnoringCase("GET")));
        assertThat(call.getHeaders(), hasSize(2));
        assertThat(call.getHeaders().get(0).getName(), is("key"));
        assertThat(call.getHeaders().get(0).getValue(), is("value1"));
        assertThat(call.getHeaders().get(1).getName(), is("key"));
        assertThat(call.getHeaders().get(1).getValue(), is("value2"));
        assertThat(call.getBody()[0], is((byte) 0xff));
    }

    @Test
    public void testFromRequestContentEmpty() throws Exception {
        DefaultFullHttpRequest req = mock(DefaultFullHttpRequest.class);

        when(req.getUri()).thenReturn("/api/p?name=%E3%81%82");
        when(req.getMethod()).thenReturn(GET);

        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("key", "value");
        when(req.headers()).thenReturn(headers);
        when(req.content()).thenReturn(null);

        Call call = Call.fromRequest(req);

        assertThat(call.getBody().length, is(0));
    }

    @Test
    public void testFromRequestNoContent() throws Exception {
        HttpRequest req = mock(HttpRequest.class);

        when(req.getUri()).thenReturn("/api/p?name=%E3%81%82");
        when(req.getMethod()).thenReturn(GET);

        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("key", "value");
        when(req.headers()).thenReturn(headers);

        Call call = Call.fromRequest(req);

        assertThat(call.getBody().length, is(0));
    }

}
