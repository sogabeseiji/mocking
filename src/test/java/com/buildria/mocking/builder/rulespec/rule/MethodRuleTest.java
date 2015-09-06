package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.builder.rulespec.rule.MethodRule;
import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.stub.Call;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodRuleTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private MethodRule target;

    @Test(expected = NullPointerException.class)
    public void testConstructorMethodNull() throws Exception {
        String path = "/api/p";
        String method = null;
        target = new MethodRule(path, method);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorUriNull() throws Exception {
        String path = null;
        String method = "get";
        target = new MethodRule(path, method);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCallNull() throws Exception {
        String path = "/api/p";
        String method = "get";
        target = new MethodRule(path, method);

        Call call = null;
        boolean actual = target.apply(call);
    }

    @Test
    public void testApplyTrue() throws Exception {
        String path = "/api/p";
        String method = "get";
        target = new MethodRule(path, method);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("get");
        when(call.getPath()).thenReturn("/api/p");

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyMethodUnmatch() throws Exception {
        String path = "/api/p";
        String method = "get";
        target = new MethodRule(path, method);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("post");
        when(call.getPath()).thenReturn("/api/p");

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

    @Test
    public void testApplyUriUnmatch() throws Exception {
        String path = "/api/p";
        String method = "get";
        target = new MethodRule(path, method);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("get");
        when(call.getPath()).thenReturn("/api/q");

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
