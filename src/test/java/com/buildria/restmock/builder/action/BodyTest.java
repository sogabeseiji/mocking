package com.buildria.restmock.builder.action;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.TestNameRule;
import com.buildria.restmock.builder.action.Action.Body;
import com.buildria.restmock.builder.action.Action.Header;
import com.buildria.restmock.serializer.ObjectSerializerContext;
import com.buildria.restmock.serializer.Person;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.MoreObjects;
import com.google.common.net.MediaType;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.HttpMethod;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.hamcrest.Matcher;
import org.junit.Rule;
import org.junit.Test;

import static com.buildria.restmock.serializer.ObjectSerializer.JACKSON;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class BodyTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private Body target;

    private final Person person = new Person("Bob", 20);

    @Test(expected = NullPointerException.class)
    public void testConstructorPathNull() throws Exception {
        Matcher<?> path = null;
        Object content = person;
        List<Action> actions = Collections.<Action>emptyList();
        Action action = new Body(path, content, actions);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorContentNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        Object content = null;
        List<Action> actions = Collections.<Action>emptyList();
        Action action = new Body(path, content, actions);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyResponseNull() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        Object content = person;
        List<Action> actions = Collections.<Action>emptyList();
        Action action = new Body(path, content, actions);
        action.apply(null, null);
    }

    @Test
    public void testApplyResponse() throws Exception {
        Matcher<?> path = equalTo("/api/p");
        Object content = person;

        List<Action> actions = new ArrayList<>();
        actions.add(new Header(equalTo("/api/p"), "Content-Type", "application/json"));

        Action action = new Body(path, content, actions);
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(req, res);

        assertThat(out, notNullValue());
        assertThat(out.headers().get("Content-Length"), is("23"));

        assertThat(out, instanceOf(DefaultFullHttpResponse.class));
        DefaultFullHttpResponse response = (DefaultFullHttpResponse) out;
        ByteBuf buf = response.content();
        String json = buf.toString(StandardCharsets.UTF_8);

        ObjectSerializerContext ctx
                = new ObjectSerializerContext(person, MediaType.JSON_UTF_8.toString());
        String expected = JACKSON.serialize(ctx);

        assertThat(json, is(expected));
    }

    @Test(expected = RestMockException.class)
    public void testApplyResponseNoContentType() throws Exception {
        StubHttpServer server = new StubHttpServer();
        Matcher<?> path = equalTo("/api/p");
        Object content = person;

        Action action = new Body(path, content, Collections.<Action>emptyList());
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(req, res);
    }

    @Test
    public void testObjects() {
        Matcher<?> path = equalTo("/api/p");
        Object content = person;
        List<Action> actions = Collections.<Action>emptyList();

        Action action = new Body(path, content, actions);

        MoreObjects.ToStringHelper answer = action.objects();
        assertThat(answer, notNullValue());
        assertThat(answer.toString(), containsString("path"));
        assertThat(answer.toString(), containsString("content"));
    }
}
