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
