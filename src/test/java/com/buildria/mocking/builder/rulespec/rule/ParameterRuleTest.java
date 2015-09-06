package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.builder.rulespec.rule.ParameterRule;
import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.stub.Call;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ParameterRuleTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private ParameterRule target;

    @Test(expected = NullPointerException.class)
    public void testConstructorKeyNull() throws Exception {
        String key = null;
        String[] values = new String[] {"values"};
        target = new ParameterRule(key, values);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValuesNull() throws Exception {
        String key = "key";
        String[] values = null;
        target = new ParameterRule(key, values);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCallNull() throws Exception {
        String key = "key";
        String[] values = new String[] {"values"};
        target = new ParameterRule(key, values);

        Call call = null;
        boolean actual = target.apply(call);
    }

    @Test
    public void testApplyOneParams() throws Exception {
        String key = "key";
        String[] values = new String[]{"value1"};
        target = new ParameterRule(key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value1"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyTwoParams() throws Exception {
        String key = "key";
        String[] values = new String[]{"value1", "value2"};
        target = new ParameterRule(key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value1", "value2"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyFalse() throws Exception {
        String key = "key";
        String[] values = new String[]{"value1"};
        target = new ParameterRule(key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value11"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

    @Test
    public void testApplyFalse2() throws Exception {
        String key = "key";
        String[] values = new String[]{"value1"};
        target = new ParameterRule(key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key1", Arrays.asList("value11"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
