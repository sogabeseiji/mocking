package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.builder.rule.NoHeaderRule;
import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.stub.Call;
import com.buildria.mocking.stub.Pair;
import java.util.ArrayList;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.mocking.http.MockingHttpHeaders.ACCEPT_CHARSET;
import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class NoHeaderRuleTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private NoHeaderRule target;

    @Test(expected = NullPointerException.class)
    public void testConstructorNameNull() throws Exception {
        String name = null;
        target = new NoHeaderRule(name);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCtxNull() throws Exception {
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new NoHeaderRule(name);

        Call call = null;
        target.apply(call);
    }

    @Test
    public void testApplyTrue() throws Exception {
        String name = CONTENT_TYPE;
        target = new NoHeaderRule(name);

        Call call = mock(Call.class);
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair(ACCEPT_CHARSET, "UTF-16BE"));
        headers.add(new Pair("X-Header", "MyHeader"));
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyFalse() throws Exception {
        String name = CONTENT_TYPE;
        target = new NoHeaderRule(name);

        Call call = mock(Call.class);
        List<Pair> headers = new ArrayList<>();
        headers.add(new Pair("X-Header", "MyHeader"));
        headers.add(new Pair(CONTENT_TYPE, "UTF-16BE"));
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
