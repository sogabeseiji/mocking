package com.buildria.restmock.builder.action;

import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.action.Action.Header;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.MoreObjects.ToStringHelper;
import io.netty.handler.codec.http.HttpRequest;
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
    public void testConstructorPathNull() throws Exception {
        Matcher<?> path = null;
        Action action = new ActionImpl(path);
    }

    @Test
    public void testIsApplicableTrue() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        Action action = new ActionImpl(path);

        boolean answer = action.isApplicable("/api/p");
        assertThat(answer, is(true));
    }

    @Test
    public void testIsApplicableFalse() throws Exception {
        Matcher<?> path = Matchers.startsWith("/api/p");
        Action action = new ActionImpl(path);

        boolean answer = action.isApplicable("/api/q");
        assertThat(answer, is(false));
    }

    @Test
    public void testGetHeaderAction() {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> path = equalTo("/api/p");

        server.addAction(new Action.StatusCode(path, 200));
        server.addAction(new Action.Header(equalTo("/api/q"), "Content-Type", "application/json"));
        server.addAction(new Action.Header(path, "Content-Type", "application/xml"));

        Action action = new ActionImpl(path);

        Header contentType = action.getHeader("/api/p", "Content-Type", server.getActions());
        assertThat(contentType, notNullValue());
        assertThat(contentType.getValue(), is("application/xml"));
    }

    @Test
    public void testGetHeaderActionCotentTypeNone() {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> path = equalTo("/api/p");

        server.addAction(new Action.Header(path, "Accept", "application/xml"));

        Action action = new ActionImpl(path);

        Header contentType = action.getHeader("/api/p", "Content-Type", server.getActions());
        assertThat(contentType, nullValue());
    }

    @Test
    public void testObjects() {
        Matcher<?> path = Matchers.startsWith("/api/p");
        Action action = new ActionImpl(path);

        ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("path"));
    }

    private static class ActionImpl extends Action {

        public ActionImpl(Matcher<?> path) {
            super(path);
        }

        @Override
        public HttpResponse apply(HttpRequest req, HttpResponse res) {
            return res;
        }

    }
}
