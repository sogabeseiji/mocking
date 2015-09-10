/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buildria.mocking.builder.action;

import com.buildria.mocking.builder.action.HeaderAction;
import com.buildria.mocking.builder.action.Action;
import com.buildria.mocking.builder.action.BodyAction;
import com.buildria.mocking.MockingException;
import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.serializer.ObjectSerializer;
import com.buildria.mocking.serializer.ObjectSerializerContext;
import com.buildria.mocking.serializer.ObjectSerializerFactory;
import com.buildria.mocking.serializer.Person;
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
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class BodyActionTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private BodyAction target;

    private final Person person = new Person("Bob", 20);

    @Test(expected = NullPointerException.class)
    public void testConstructorPathNull() throws Exception {
        String path = null;
        Object content = person;
        List<Action> actions = Collections.<Action>emptyList();
        Action action = new BodyAction(path, content, actions);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorContentNull() throws Exception {
        String path = "/api/p";
        Object content = null;
        List<Action> actions = Collections.<Action>emptyList();
        Action action = new BodyAction(path, content, actions);
    }

    @Test(expected = NullPointerException.class)
    public void testConstructorActionsNull() throws Exception {
        String path = "/api/p";
        Object content = person;
        List<Action> actions = null;
        Action action = new BodyAction(path, content, actions);
    }

    @Test(expected = NullPointerException.class)
    public void testApplyResponseNull() throws Exception {
        String path = "/api/p";
        Object content = person;
        List<Action> actions = Collections.<Action>emptyList();
        Action action = new BodyAction(path, content, actions);
        action.apply(null, null);
    }

    @Test
    public void testApplyResponseUTF8() throws Exception {
        String path = "/api/p";
        Object content = person;

        List<Action> actions = new ArrayList<>();
        actions.add(new HeaderAction("/api/p", "Content-Type", "application/json; charset=UTF-8"));

        Action action = new BodyAction(path, content, actions);
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(req, res);

        assertThat(out, notNullValue());

        assertThat(out, instanceOf(DefaultFullHttpResponse.class));
        DefaultFullHttpResponse response = (DefaultFullHttpResponse) out;
        ByteBuf buf = response.content();
        byte[] json = buf.toString(StandardCharsets.UTF_8).getBytes();

        assertThat(Integer.valueOf(out.headers().get("Content-Length")), is(json.length));

        ObjectSerializerContext ctx
                = new ObjectSerializerContext("application/json; charset=UTF-8");
        ObjectSerializer serializer = ObjectSerializerFactory.create(ctx);
        byte[] expected = serializer.serialize(person);

        assertThat(json, is(expected));
    }

    @Test
    public void testApplyResponseUTF16BE() throws Exception {
        String path = "/api/p";
        Object content = person;

        List<Action> actions = new ArrayList<>();
        actions.add(new HeaderAction("/api/p", "Content-Type", "application/json; charset=UTF-16BE"));

        Action action = new BodyAction(path, content, actions);
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(req, res);

        assertThat(out, notNullValue());

        assertThat(out, instanceOf(DefaultFullHttpResponse.class));
        DefaultFullHttpResponse response = (DefaultFullHttpResponse) out;
        ByteBuf buf = response.content();
        byte[] json = buf.toString(StandardCharsets.UTF_8).getBytes();

        assertThat(Integer.valueOf(out.headers().get("Content-Length")), is(json.length));

        ObjectSerializerContext ctx
                = new ObjectSerializerContext("application/json; charset=UTF-16BE");
        ObjectSerializer serializer = ObjectSerializerFactory.create(ctx);
        byte[] expected = serializer.serialize(person);

        assertThat(json, is(expected));
    }

    @Test(expected = MockingException.class)
    public void testApplyResponseNoContentType() throws Exception {
        String path = "/api/p";
        Object content = person;

        Action action = new BodyAction(path, content, Collections.<Action>emptyList());
        HttpRequest req = new DefaultHttpRequest(HttpVersion.HTTP_1_1, HttpMethod.GET, "/api/p");
        HttpResponse res = new DefaultHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
        HttpResponse out = action.apply(req, res);
    }

}
