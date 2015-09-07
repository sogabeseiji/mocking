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
package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.stub.Call;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParameterRuleTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private ParameterRule target;

    @Test(expected = NullPointerException.class)
    public void testConstructorKeyNull() throws Exception {
        String key = null;
        String[] values = new String[] {"values"};
        target = new ParameterRule(key, values);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValuesNull() throws Exception {
        String key = "key";
        String[] values = null;
        target = new ParameterRule(key, values);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCallNull() throws Exception {
        String key = "key";
        String[] values = new String[] {"values"};
        target = new ParameterRule(key, values);

        Call call = null;
        boolean actual = target.apply(call);
    }

    @Test
    public void testApplyOneParams() throws Exception {
        String key = "key";
        String[] values = new String[]{"value1"};
        target = new ParameterRule(key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value1"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyTwoParams() throws Exception {
        String key = "key";
        String[] values = new String[]{"value1", "value2"};
        target = new ParameterRule(key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value1", "value2"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyFalse() throws Exception {
        String key = "key";
        String[] values = new String[]{"value1"};
        target = new ParameterRule(key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value11"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

    @Test
    public void testApplyFalse2() throws Exception {
        String key = "key";
        String[] values = new String[]{"value1"};
        target = new ParameterRule(key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key1", Arrays.asList("value11"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
