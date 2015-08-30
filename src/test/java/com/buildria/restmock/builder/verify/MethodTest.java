package com.buildria.restmock.builder.verify;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.verify.Verifier.Method;
import com.buildria.restmock.stub.Call;
import com.buildria.restmock.stub.StubHttpServer;
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
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String method = null;
        target = new Method(server, path, method);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorUriNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = null;
        String method = "get";
        target = new Method(server, path, method);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCallNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String method = "get";
        target = new Method(server, path, method);

        Call call = null;
        target.apply(call);
    }

    @Test
    public void testApplyTrue() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String method = "get";
        target = new Method(server, path, method);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("get");
        when(call.getPath()).thenReturn("/api/p");

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyMethodUnmatch() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String method = "get";
        target = new Method(server, path, method);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("post");
        when(call.getPath()).thenReturn("/api/p");

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

    @Test
    public void testApplyUriUnmatch() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String method = "get";
        target = new Method(server, path, method);

        Call call = mock(Call.class);
        when(call.getMethod()).thenReturn("get");
        when(call.getPath()).thenReturn("/api/q");

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
