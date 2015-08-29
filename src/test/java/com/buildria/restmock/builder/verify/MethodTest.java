package com.buildria.restmock.builder.verify;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.verify.Verifier.Method;
import com.buildria.restmock.stub.Call;
import org.junit.Rule;
import org.junit.Test;

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

}
