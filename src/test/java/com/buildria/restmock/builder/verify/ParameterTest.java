package com.buildria.restmock.builder.verify;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.verify.Rule.Parameter;
import com.buildria.restmock.stub.Call;
import com.buildria.restmock.stub.StubHttpServer;
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

public class ParameterTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private Parameter target;

    @Test(expected = NullPointerException.class)
    public void testConstructorKeyNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String key = null;
        String[] values = new String[] {"values"};
        target = new Parameter(server, path, key, values);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValuesNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String key = "key";
        String[] values = null;
        target = new Parameter(server, path, key, values);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCallNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String key = "key";
        String[] values = new String[] {"values"};
        target = new Parameter(server, path, key, values);

        Call call = null;
        target.apply(call);
    }

    @Test
    public void testApplyOneParams() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String key = "key";
        String[] values = new String[]{"value1"};
        target = new Parameter(server, path, key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value1"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyTwoParams() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String key = "key";
        String[] values = new String[]{"value1", "value2"};
        target = new Parameter(server, path, key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value1", "value2"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyFalse() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String key = "key";
        String[] values = new String[]{"value1"};
        target = new Parameter(server, path, key, values);

        Call call = mock(Call.class);
        Map<String, List<String>> params = new HashMap<>();
        params.put("key", Arrays.asList("value11"));

        when(call.getParameters()).thenReturn(params);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
