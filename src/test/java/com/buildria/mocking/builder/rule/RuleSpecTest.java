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
package com.buildria.mocking.builder.rule;

import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.stub.Call;
import com.buildria.mocking.stub.Pair;
import com.google.common.net.MediaType;
import java.util.ArrayList;
import java.util.List;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.containsString;
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

        target = new RuleSpecImpl(calls);
        target.validate(new ParameterRule("name", new String[] {"Bob"}));
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
            target = new RuleSpecImpl(calls);
            target.validate(new ParameterRule("name", new String[] {"Bob"}));
            fail();
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("Parameter"));
            assertThat(e.getMessage(), containsString("name"));
            assertThat(e.getMessage(), containsString("Bob"));
        }
    }

    private static class RuleSpecImpl extends RuleSpec {

        public RuleSpecImpl(List<Call> calls) {
            super(calls);
        }

    }

    private static final Logger LOG = LoggerFactory.getLogger(RuleSpecTest.class);
}
