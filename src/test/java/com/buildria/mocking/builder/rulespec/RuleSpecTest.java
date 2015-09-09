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
package com.buildria.mocking.builder.rulespec;

import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.builder.rulespec.rule.BodyRule;
import com.buildria.mocking.builder.rulespec.rule.HeaderRule;
import com.buildria.mocking.builder.rulespec.rule.MethodRule;
import com.buildria.mocking.builder.rulespec.rule.ParameterRule;
import com.buildria.mocking.stub.Call;
import com.buildria.mocking.stub.Pair;
import com.google.common.net.MediaType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RuleSpecTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private static final String EXPECTED_JSON
            = "{ \"name\": \"Bob\", \"old\": 19}";
    private RuleSpec target;

    @Before
    public void setUp() throws Exception {
        target = new RuleSpecImpl();
        target.addRule(new MethodRule("/api/p", "get"));
        target.addRule(new ParameterRule("name", new String[] {"Bob"}));
        target.addRule(new HeaderRule(CONTENT_TYPE, equalTo("application/json")));
        target.addRule(new BodyRule("name", is("Bob")));
    }

    @Test
    public void testValidateMatch() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/p");
        when(c1.getMethod()).thenReturn("get");
        when(c1.getBody()).thenReturn(EXPECTED_JSON.getBytes("UTF-8"));

        List<Pair> params = new ArrayList<>();
        params.add(new Pair("name", "Bob"));
        when(c1.getParameters()).thenReturn(params);

        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/json"));
        when(c1.getHeaders()).thenReturn(headers);
        when(c1.getContentType()).thenReturn(MediaType.parse("application/json"));
        calls.add(c1);

        target.validate(calls);
    }

    @Test
    public void testValidateUnMatch1() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/p");
        when(c1.getMethod()).thenReturn("post");
        when(c1.getBody()).thenReturn(EXPECTED_JSON.getBytes("UTF-8"));

        List<Pair> params = new ArrayList<>();
        params.add(new Pair("name", "bob"));
        when(c1.getParameters()).thenReturn(params);

        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/json"));
        when(c1.getHeaders()).thenReturn(headers);
        when(c1.getContentType()).thenReturn(MediaType.parse("application/json"));
        calls.add(c1);

        try {
            target.validate(calls);
            fail();
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("[Method]"));
            assertThat(e.getMessage(), containsString("get"));
            assertThat(e.getMessage(), containsString("/api/p"));
        }
    }

    @Test
    public void testValidateUnMatch2() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/p");
        when(c1.getMethod()).thenReturn("get");
        when(c1.getBody()).thenReturn(EXPECTED_JSON.getBytes("UTF-8"));

        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/json"));
        when(c1.getHeaders()).thenReturn(headers);
        when(c1.getContentType()).thenReturn(MediaType.parse("application/json"));
        calls.add(c1);

        try {
            target.validate(calls);
            fail();
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("[Parameter]"));
            assertThat(e.getMessage(), containsString("name"));
            assertThat(e.getMessage(), containsString("Bob"));
        }
    }

    @Test
    public void testValidateUnMatch3() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/q");
        when(c1.getMethod()).thenReturn("get");

        List<Pair> params = new ArrayList<>();
        params.add(new Pair("name", "Bob"));
        when(c1.getParameters()).thenReturn(params);

        when(c1.getBody()).thenReturn(EXPECTED_JSON.getBytes("UTF-8"));

        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/json"));
        when(c1.getHeaders()).thenReturn(headers);
        when(c1.getContentType()).thenReturn(MediaType.parse("application/json"));
        calls.add(c1);

        try {
            target.validate(calls);
            fail();
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("[Method]"));
            assertThat(e.getMessage(), containsString("/api/p"));
        }
    }

    @Test
    public void testValidateUnMatch4() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/p");
        when(c1.getMethod()).thenReturn("get");

        List<Pair> params = new ArrayList<>();
        params.add(new Pair("name", "Bob"));
        when(c1.getParameters()).thenReturn(params);

        when(c1.getBody()).thenReturn(EXPECTED_JSON.getBytes("UTF-8"));

        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/xml"));
        when(c1.getHeaders()).thenReturn(headers);
        when(c1.getContentType()).thenReturn(MediaType.parse("application/xml"));
        calls.add(c1);

        try {
            target.validate(calls);
            fail();
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("[Header]"));
            assertThat(e.getMessage(), containsString(CONTENT_TYPE));
            assertThat(e.getMessage(), containsString("application/json"));
        }
    }

    @Test
    public void testValidateBodyUnMatch() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/p");
        when(c1.getMethod()).thenReturn("get");
        when(c1.getBody()).thenReturn("".getBytes("UTF-8"));

        List<Pair> params = new ArrayList<>();
        params.add(new Pair("name", "Bob"));
        when(c1.getParameters()).thenReturn(params);

        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/json"));
        when(c1.getHeaders()).thenReturn(headers);
        when(c1.getContentType()).thenReturn(MediaType.parse("application/json"));
        calls.add(c1);

        try {
            target.validate(calls);
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("[Body]"));
            assertThat(e.getMessage(), containsString("name"));
            assertThat(e.getMessage(), containsString("Bob"));
        }
    }

    private static class RuleSpecImpl extends RuleSpec {

        public RuleSpecImpl() {
            super(new ArrayList<com.buildria.mocking.builder.rulespec.rule.Rule>());
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(RuleSpecTest.class);
}
