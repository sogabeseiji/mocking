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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.builder.rule.HeaderRule;
import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.stub.Call;
import com.buildria.mocking.stub.Pair;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HeaderRuleTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private HeaderRule target;

    @Test(expected = NullPointerException.class)
    public void testConstructorNameNull() throws Exception {
        String name = null;
        Matcher<?> value = equalTo("application/json");
        target = new HeaderRule(name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValueNull() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = null;
        target = new HeaderRule(name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCtxNull() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new HeaderRule(name, value);

        Call call = null;
        target.apply(call);
    }

    @Test
    public void testApplyTrue() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new HeaderRule(name, value);

        Call call = mock(Call.class);
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/json"));
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyTrue2() throws Exception {
        String name = CONTENT_TYPE;
        @SuppressWarnings("unchecked")
        Matcher<?> value = anyOf(
                equalTo("application/json"), equalTo("application/xml"));
        target = new HeaderRule(name, value);

        Call call = mock(Call.class);
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/xml"));
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyFalse() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new HeaderRule(name, value);

        Call call = mock(Call.class);
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/xml"));
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

    @Test
    public void testApplyMultipleHeaders() throws Exception {
        String name = "X-Header";
        @SuppressWarnings("unchecked")
        Matcher<?> value = anyOf(
                equalTo("value1"), equalTo("value2"));
        target = new HeaderRule(name, value);

        Call call = mock(Call.class);
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(name, "value1"));
        headers.add(new Pair(name, "value2"));
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }
}
