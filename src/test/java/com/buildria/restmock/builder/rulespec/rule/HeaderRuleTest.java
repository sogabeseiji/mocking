package com.buildria.restmock.builder.rulespec.rule;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.http.RMHttpHeaders;
import com.buildria.restmock.stub.Call;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class HeaderRuleTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private HeaderRule target;

    @Test(expected = NullPointerException.class)
    public void testConstructorNameNull() throws Exception {
        String name = null;
        Matcher<?> value = equalTo("application/json");
        target = new HeaderRule(name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValueNull() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = null;
        target = new HeaderRule(name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCtxNull() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new HeaderRule(name, value);

        Call call = null;
        target.apply(call);
    }

    @Test
    public void testApplyTrue() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new HeaderRule(name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(RMHttpHeaders.CONTENT_TYPE, "application/json");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyTrue2() throws Exception {
        String name = CONTENT_TYPE;
        @SuppressWarnings("unchecked")
        Matcher<?> value = anyOf(equalTo("application/json"), equalTo("application/xml"));
        target = new HeaderRule(name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/xml");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyFalse() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new HeaderRule(name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/xml");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
