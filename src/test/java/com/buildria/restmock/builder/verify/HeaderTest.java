package com.buildria.restmock.builder.verify;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.verify.Verifier.Header;
import com.buildria.restmock.http.RM_HttpHeaders;
import com.buildria.restmock.stub.Call;
import com.buildria.restmock.stub.StubHttpServer;
import java.util.HashMap;
import java.util.Map;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.http.RM_HttpHeaders.CONTENT_TYPE;
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
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String name = null;
        Matcher<?> value = equalTo("application/json");
        target = new Header(server, path, name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorValueNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String name = CONTENT_TYPE;
        Matcher<?> value = null;
        target = new Header(server, path, name, value);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyCallNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new Header(server, path, name, value);

        Call call = null;
        target.apply(call);
    }

    @Test
    public void testApplyTrue() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new Header(server, path, name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(RM_HttpHeaders.CONTENT_TYPE, "application/json");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyTrue2() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String name = CONTENT_TYPE;
        @SuppressWarnings("unchecked")
        Matcher<?> value = anyOf(equalTo("application/json"), equalTo("application/xml"));
        target = new Header(server, path, name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/xml");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(true));
    }

    @Test
    public void testApplyFalse() throws Exception {
        StubHttpServer server = new StubHttpServer();
        String path = "/api/p";
        String name = CONTENT_TYPE;
        Matcher<?> value = equalTo("application/json");
        target = new Header(server, path, name, value);

        Call call = mock(Call.class);
        Map<String, String> headers = new HashMap<>();
        headers.put(CONTENT_TYPE, "application/xml");
        when(call.getHeaders()).thenReturn(headers);

        boolean actual = target.apply(call);

        assertThat(actual, is(false));
    }

}
