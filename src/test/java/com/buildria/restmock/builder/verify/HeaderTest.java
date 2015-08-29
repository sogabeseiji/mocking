package com.buildria.restmock.builder.verify;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.verify.Verifier.Header;
import com.buildria.restmock.stub.Call;
import com.google.common.net.HttpHeaders;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class HeaderTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private Header target;

    @Test(expected = NullPointerException.class)
    public void testConstructorNameNull() throws Exception {
        String name = null;
        Matcher<?> value = equalTo("application/json");
        target = new Header(name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValueNull() throws Exception {
        String name = HttpHeaders.CONTENT_TYPE;
        Matcher<?> value = null;
        target = new Header(name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCallNull() throws Exception {
        String name = HttpHeaders.CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new Header(name, value);

        Call call = null;
        target.apply(call);
    }

}
