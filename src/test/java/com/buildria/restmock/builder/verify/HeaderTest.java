package com.buildria.restmock.builder.verify;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.verify.Rule.Header;
import com.buildria.restmock.builder.verify.Rule.RuleContext;
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

public class HeaderTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private Header target;

    @Test(expected = NullPointerException.class)
    public void testConstructorNameNull() throws Exception {
        String path = "/api/p";
        String name = null;
        Matcher<?> value = equalTo("application/json");
        target = new Header(path, name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValueNull() throws Exception {
        String path = "/api/p";
        String name = CONTENT_TYPE;
        Matcher<?> value = null;
        target = new Header(path, name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCtxNull() throws Exception {
        String path = "/api/p";
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new Header(path, name, value);

        RuleContext ctx  = null;
        target.apply(ctx);
    }

    @Test
    public void testApplyTrue() throws Exception {
        String path = "/api/p";
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new Header(path, name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(RMHttpHeaders.CONTENT_TYPE, "application/json");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(new RuleContext(call, null));

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyTrue2() throws Exception {
        String path = "/api/p";
        String name = CONTENT_TYPE;
        @SuppressWarnings("unchecked")
        Matcher<?> value = anyOf(equalTo("application/json"), equalTo("application/xml"));
        target = new Header(path, name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/xml");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(new RuleContext(call, null));

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyFalse() throws Exception {
        String path = "/api/p";
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new Header(path, name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/xml");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(new RuleContext(call, null));

        assertThat(actual, is(false));
    }

}
