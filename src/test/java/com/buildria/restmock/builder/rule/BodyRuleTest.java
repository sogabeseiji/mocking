package com.buildria.restmock.builder.rule;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.rule.Rule.RuleContext;
import com.buildria.restmock.stub.Call;
import com.google.common.net.MediaType;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.hamcrest.Matcher;
import org.junit.Test;

import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BodyRuleTest {

    @org.junit.Rule
    public TestNameRule testNameRule = new TestNameRule();

    private BodyRule target;

    private static final String EXPECTED_XML
            = "<person>"
            + "  <name>あいうえお</name>"
            + "  <old>19</old>"
            + "</person>";

    private static final String EXPECTED_JSON
            = "{ \"name\": \"Bob\", \"old\": 19}";

    @Test(expected = NullPointerException.class)
    public void testConstructorPathNull() throws Exception {
        String path = null;
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorMatcherNull() throws Exception {
        String path = "person.name";
        Matcher<?> matcher = null;
        target = new BodyRule(path, matcher);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyNull() throws Exception {
        String path = "person.name";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);
        target.apply(null);
    }

    @Test
    public void testApplyBodyNull() throws Exception {
        String path = "person.name";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(null);
        List<Rule> rules = Collections.emptyList();
        RuleContext ctx = new RuleContext(call, rules);

        boolean answer = target.apply(ctx);

        assertThat(answer, is(false));
    }

    @Test
    public void testApplyBodyEmpty() throws Exception {
        String path = "person.name";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(new byte[0]);
        List<Rule> rules = Collections.emptyList();
        RuleContext ctx = new RuleContext(call, rules);

        boolean answer = target.apply(ctx);

        assertThat(answer, is(false));
    }

    @Test
    public void testApplyBodyXmlMatch() throws Exception {
        String path = "person.old";
        Matcher<?> matcher = equalTo("19");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(EXPECTED_XML.getBytes(StandardCharsets.UTF_8));
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/xml");
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(MediaType.parse("application/xml"));
        List<Rule> rules = Collections.emptyList();
        RuleContext ctx = new RuleContext(call, rules);

        boolean answer = target.apply(ctx);

        assertThat(answer, is(true));
    }

    @Test
    public void testApplyBodyXmlUnMatch() throws Exception {
        String path = "person.old";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(EXPECTED_XML.getBytes(StandardCharsets.UTF_8));
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/xml");
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(MediaType.parse("application/xml"));
        List<Rule> rules = Collections.emptyList();
        RuleContext ctx = new RuleContext(call, rules);

        boolean answer = target.apply(ctx);

        assertThat(answer, is(false));
    }

    @Test
    public void testApplyBodyJsonMatch() throws Exception {
        String path = "old";
        Matcher<?> matcher = equalTo(19);
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(EXPECTED_JSON.getBytes(StandardCharsets.UTF_8));
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/json");
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(MediaType.parse("application/json"));
        List<Rule> rules = Collections.emptyList();
        RuleContext ctx = new RuleContext(call, rules);

        boolean answer = target.apply(ctx);

        assertThat(answer, is(true));
    }

    @Test
    public void testApplyBodyJsonUnMatch() throws Exception {
        String path = "old";
        Matcher<?> matcher = equalTo(20);
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(EXPECTED_JSON.getBytes(StandardCharsets.UTF_8));
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/json");
        when(call.getHeaders()).thenReturn(headers);
        when(call.getContentType()).thenReturn(MediaType.parse("application/json"));
        List<Rule> rules = Collections.emptyList();
        RuleContext ctx = new RuleContext(call, rules);

        boolean answer = target.apply(ctx);

        assertThat(answer, is(false));
    }

    @Test(expected = RestMockException.class)
    public void testApplyNotSupported() throws Exception {
        String path = "old";
        Matcher<?> matcher = equalTo(20);
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(EXPECTED_JSON.getBytes(StandardCharsets.UTF_8));
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "image/png");
        when(call.getContentType()).thenReturn(MediaType.parse("image/png"));
        when(call.getHeaders()).thenReturn(headers);
        List<Rule> rules = Collections.emptyList();
        RuleContext ctx = new RuleContext(call, rules);

        boolean answer = target.apply(ctx);
    }

    @Test(expected = RestMockException.class)
    public void testApplyNoConentType() throws Exception {
        String path = "person.old";
        Matcher<?> matcher = equalTo("20");
        target = new BodyRule(path, matcher);

        Call call = mock(Call.class);
        when(call.getBody()).thenReturn(EXPECTED_XML.getBytes(StandardCharsets.UTF_8));
        List<Rule> rules = Collections.emptyList();
        RuleContext ctx = new RuleContext(call, rules);

        boolean answer = target.apply(ctx);
    }

}