package com.buildria.mocking.stub;

import com.buildria.mocking.TestNameRule;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpRequest;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import java.util.Arrays;
import org.junit.Rule;
import org.junit.Test;

import static io.netty.handler.codec.http.HttpMethod.GET;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CallTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = NullPointerException.class)
    public void testFromRequestRequestNull() throws Exception {
        HttpRequest req = null;
        Call.fromRequest(req);
    }

    @Test
    public void testFromRequest() throws Exception {
        DefaultFullHttpRequest req = mock(DefaultFullHttpRequest.class);

        when(req.getUri()).thenReturn("/api/p?name=%E3%81%82");
        when(req.getMethod()).thenReturn(GET);

        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("key", "value");
        when(req.headers()).thenReturn(headers);

        ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();
        buf.writeByte((byte) 0xff);
        when(req.content()).thenReturn(buf);

        Call call = Call.fromRequest(req);

        assertThat(call.getPath(), is("/api/p"));
        assertThat(call.getParameters(), hasEntry("name", Arrays.asList("\u3042")));
        assertThat(call.getMethod(), is(equalToIgnoringCase("GET")));
        assertThat(call.getHeaders().get("key"), is("value"));
        assertThat(call.getBody()[0], is((byte) 0xff));
    }

    @Test
    public void testFromRequestContentEmpty() throws Exception {
        DefaultFullHttpRequest req = mock(DefaultFullHttpRequest.class);

        when(req.getUri()).thenReturn("/api/p?name=%E3%81%82");
        when(req.getMethod()).thenReturn(GET);

        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("key", "value");
        when(req.headers()).thenReturn(headers);
        when(req.content()).thenReturn(null);

        Call call = Call.fromRequest(req);

        assertThat(call.getBody().length, is(0));
    }

    @Test
    public void testFromRequestNoContent() throws Exception {
        HttpRequest req = mock(HttpRequest.class);

        when(req.getUri()).thenReturn("/api/p?name=%E3%81%82");
        when(req.getMethod()).thenReturn(GET);

        HttpHeaders headers = new DefaultHttpHeaders();
        headers.add("key", "value");
        when(req.headers()).thenReturn(headers);

        Call call = Call.fromRequest(req);

        assertThat(call.getBody().length, is(0));
    }

}
