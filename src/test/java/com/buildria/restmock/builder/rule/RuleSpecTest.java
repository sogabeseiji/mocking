package com.buildria.restmock.builder.rule;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.rule.Rule.Header;
import com.buildria.restmock.builder.rule.Rule.Method;
import com.buildria.restmock.builder.rule.Rule.Parameter;
import com.buildria.restmock.stub.Call;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RuleSpecTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private RuleSpec target;

    @Before
    public void setUp() throws Exception {
        target = new RuleSpecImpl();
        target.addRule(new Method("/api/p", "get"));
        target.addRule(new Parameter("name", new String[] {"Bob"}));
        target.addRule(new Header(CONTENT_TYPE, equalTo("application/json")));
    }

    @Test
    public void testValidateMatch() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/p");
        when(c1.getMethod()).thenReturn("get");

        Map<String, List<String>> params = new HashMap<>();
        params.put("name", Arrays.asList("Bob"));
        when(c1.getParameters()).thenReturn(params);

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/json");
        when(c1.getHeaders()).thenReturn(headers);
        calls.add(c1);

        target.validate(calls);
    }

    @Test
    public void testValidateUnMatch1() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/p");
        when(c1.getMethod()).thenReturn("post");

        Map<String, List<String>> params = new HashMap<>();
        params.put("name", Arrays.asList("bob"));
        when(c1.getParameters()).thenReturn(params);

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/json");
        when(c1.getHeaders()).thenReturn(headers);
        calls.add(c1);

        try {
            target.validate(calls);
            fail();
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("[Method]"));
            assertThat(e.getMessage(), containsString("get"));
            assertThat(e.getMessage(), containsString("/api/p"));
        }
    }

    @Test
    public void testValidateUnMatch2() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/p");
        when(c1.getMethod()).thenReturn("get");

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/json");
        when(c1.getHeaders()).thenReturn(headers);
        calls.add(c1);

        try {
            target.validate(calls);
            fail();
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("[Parameter]"));
            assertThat(e.getMessage(), containsString("name"));
            assertThat(e.getMessage(), containsString("Bob"));
        }
    }

    @Test
    public void testValidateUnMatch3() throws Exception {
        List<Call> calls = new ArrayList<>();
        Call c1 = mock(Call.class);
        when(c1.getPath()).thenReturn("/api/q");
        when(c1.getMethod()).thenReturn("get");

        Map<String, List<String>> params = new HashMap<>();
        params.put("name", Arrays.asList("bob"));
        when(c1.getParameters()).thenReturn(params);

        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/json");
        when(c1.getHeaders()).thenReturn(headers);
        calls.add(c1);

        try {
            target.validate(calls);
            fail();
        } catch (AssertionError e) {
            LOG.warn(e.getMessage());
            assertThat(e.getMessage(), containsString("[Method]"));
            assertThat(e.getMessage(), containsString("/api/p"));
        }
    }

    private static class RuleSpecImpl extends RuleSpec {
        //
    }

    private static final Logger LOG = LoggerFactory.getLogger(RuleSpecTest.class);
}
