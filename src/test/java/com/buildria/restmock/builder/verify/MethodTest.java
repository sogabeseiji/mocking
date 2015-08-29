package com.buildria.restmock.builder.verify;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.verify.Verifier.Method;
import com.buildria.restmock.stub.Call;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MethodTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private Method target;

    @Test(expected = NullPointerException.class)
    public void testConstructorMethodNull() throws Exception {
        String method = null;
        String uri = "/api/p";
        target = new Method(method, uri);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorUriNull() throws Exception {
        String method = "get";
        String uri = null;
        target = new Method(method, uri);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCallNull() throws Exception {
        String method = "get";
        String uri = "/api/p";
        target = new Method(method, uri);

        Call call = null;
        target.apply(call);
    }

    @Test
    public void testApplyTrue() throws Exception {
        String method = "get";
        String uri = "/api/p";
        target = new Method(method, uri);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("get");
        when(call.getUri()).thenReturn("/api/p");

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyMethodUnmatch() throws Exception {
        String method = "get";
        String uri = "/api/p";
        target = new Method(method, uri);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("post");
        when(call.getUri()).thenReturn("/api/p");

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

    @Test
    public void testApplyUriUnmatch() throws Exception {
        String method = "get";
        String uri = "/api/p";
        target = new Method(method, uri);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("get");
        when(call.getUri()).thenReturn("/api/q");

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
