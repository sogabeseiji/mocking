package com.buildria.restmock.builder.stub;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.stub.Action.HeaderAction;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.MoreObjects.ToStringHelper;
import io.netty.handler.codec.http.HttpResponse;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;

/**
 * Test for Action.
 *
 * @author Seiji Sogabe
 */
public class ActionTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = NullPointerException.class)
    public void testConstructorServerNull() throws Exception {
        StubHttpServer server = null;
        Matcher<?> uri = equalTo("/api/p");
        Action action = new ActionImpl(server, uri);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorUriNull() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = null;
        Action action = new ActionImpl(server, uri);
    }

    @Test
    public void testIsApplicableTrue() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");
        Action action = new ActionImpl(server, uri);

        boolean answer = action.isApplicable("/api/p");
        assertThat(answer, is(true));
    }

    @Test
    public void testIsApplicableFalse() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = Matchers.startsWith("/api/p");
        Action action = new ActionImpl(server, uri);

        boolean answer = action.isApplicable("/api/q");
        assertThat(answer, is(false));
    }

    @Test
    public void testGetContentType() {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");

        server.addAction(new Action.StatusCodeAction(server, uri, 200));
        server.addAction(new Action.HeaderAction(server, uri, "Content-Type", "application/xml"));

        Action action = new ActionImpl(server, uri);

        HeaderAction contentType = action.getContentType();
        assertThat(contentType, notNullValue());
        assertThat(contentType.getValue(), is("application/xml"));
    }

    @Test
    public void testGetContentTypeNone() {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = equalTo("/api/p");

        server.addAction(new Action.StatusCodeAction(server, uri, 200));
        server.addAction(new Action.HeaderAction(server, uri, "Accept", "application/xml"));

        Action action = new ActionImpl(server, uri);

        HeaderAction contentType = action.getContentType();
        assertThat(contentType, nullValue());
    }

    @Test
    public void testObjects() {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> uri = Matchers.startsWith("/api/p");
        Action action = new ActionImpl(server, uri);

        ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("uri"));
    }

    private static class ActionImpl extends Action {

        public ActionImpl(StubHttpServer server, Matcher<?> uri) {
            super(server, uri);
        }

        @Override
        public HttpResponse apply(HttpResponse input) {
            return input;
        }

    }
}
