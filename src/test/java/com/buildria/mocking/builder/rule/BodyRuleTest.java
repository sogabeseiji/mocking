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

import com.buildria.mocking.MockingException;
import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.stub.Call;
import com.buildria.mocking.stub.Pair;
import com.google.common.net.MediaType;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Test;

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BodyRuleTest {

    @org.junit.Rule
    public TestNameRule testNameRule = new TestNameRule();

    private BodyRule target;

    private static final String EXPECTED_XML
            = "<person>"
            + "  <name>あいうえお</name>"
            + "  <old>19</old>"
            + "</person>";

    private static final String EXPECTED_JSON
            = "{ \"name\": \"Bob\", \"old\": 19}";

    @Test(expected = NullPointerException.class)
    public void testConstructorPathNull() throws Exception {
        String path = null;
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorMatcherNull() throws Exception {
        String path = "person.name";
        Matcher<?> matcher = null;
        target = new BodyRule(path, matcher);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyNull() throws Exception {
        String path = "person.name";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);
        target.apply(null);
    }

    @Test
    public void testApplyBodyNull() throws Exception {
        String path = "person.name";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(null);

        boolean answer = target.apply(call);

        assertThat(answer, is(false));
    }

    @Test
    public void testApplyBodyEmpty() throws Exception {
        String path = "person.name";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(new byte[0]);

        boolean answer = target.apply(call);

        assertThat(answer, is(false));
    }

    @Test
    public void testApplyBodyXmlMatch() throws Exception {
        String path = "person.old";
        Matcher<?> matcher = equalTo("19");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(
                EXPECTED_XML.getBytes(StandardCharsets.UTF_8));

        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/xml"));
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(
                MediaType.parse("application/xml"));

        boolean answer = target.apply(call);

        assertThat(answer, is(true));
    }

    @Test
    public void testApplyBodyXmlUnMatch() throws Exception {
        String path = "person.old";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(
                EXPECTED_XML.getBytes(StandardCharsets.UTF_8));
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/xml"));
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(
                MediaType.parse("application/xml"));

        boolean answer = target.apply(call);

        assertThat(answer, is(false));
    }

    @Test
    public void testApplyBodyJsonMatch() throws Exception {
        String path = "old";
        Matcher<?> matcher = equalTo(19);
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(
                EXPECTED_JSON.getBytes(StandardCharsets.UTF_8));
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/json"));
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(
                MediaType.parse("application/json"));

        boolean answer = target.apply(call);

        assertThat(answer, is(true));
    }

    @Test
    public void testApplyBodyJsonUnMatch() throws Exception {
        String path = "old";
        Matcher<?> matcher = equalTo(20);
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(
                EXPECTED_JSON.getBytes(StandardCharsets.UTF_8));
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "application/json"));
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(
                MediaType.parse("application/json"));

        boolean answer = target.apply(call);

        assertThat(answer, is(false));
    }

    @Test(expected = MockingException.class)
    public void testApplyNotSupported() throws Exception {
        String path = "old";
        Matcher<?> matcher = equalTo(20);
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(
                EXPECTED_JSON.getBytes(StandardCharsets.UTF_8));
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(CONTENT_TYPE, "image/png"));
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(
                MediaType.parse("image/png"));

        boolean answer = target.apply(call);
    }

    @Test(expected = MockingException.class)
    public void testApplyNoConentType() throws Exception {
        String path = "person.old";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(
                EXPECTED_XML.getBytes(StandardCharsets.UTF_8));

        boolean answer = target.apply(call);
    }

}
