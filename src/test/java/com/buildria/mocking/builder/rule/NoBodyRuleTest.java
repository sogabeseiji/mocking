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
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoBodyRuleTest {
    
    @org.junit.Rule
    public TestNameRule testNameRule = new TestNameRule();

    private NoBodyRule target;

    @Test(expected = NullPointerException.class)
    public void testApplyCtxNull() throws Exception {
        target = new NoBodyRule();
        target.apply(null);
    }

    @Test
    public void testApplyTrueNull() throws Exception {
        target = new NoBodyRule();

        Call call = mock(Call.class);
        byte[] body = null;
        when(call.getBody()).thenReturn(body);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyTrueEmpty() throws Exception {
        target = new NoBodyRule();

        Call call = mock(Call.class);
        byte[] body = new byte[0];
        when(call.getBody()).thenReturn(body);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }
    
    @Test
    public void testApplyFalse() throws Exception {
        target = new NoBodyRule();

        Call call = mock(Call.class);
        byte[] body = "body".getBytes();
        when(call.getBody()).thenReturn(body);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }    
}
