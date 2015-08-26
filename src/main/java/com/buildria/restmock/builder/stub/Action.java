package com.buildria.restmock.builder.stub;

import com.buildria.restmock.Function;
import com.buildria.restmock.RestMockException;
import com.buildria.restmock.serialize.ObjectSerializeContext;
import com.buildria.restmock.serialize.ObjectSerializer;
import com.buildria.restmock.serialize.ObjectSerializerStrategy;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.net.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.DatatypeConverter;
import org.hamcrest.Matcher;

public abstract class Action implements Function<HttpResponse, HttpResponse> {

    protected final Matcher<?> uri;

    protected final StubHttpServer server;

    public Action(StubHttpServer server, Matcher<?> uri) {
        this.server = Objects.requireNonNull(server);
        this.uri = Objects.requireNonNull(uri);
    }

    public Matcher<?> getUri() {
        return uri;
    }

    public boolean isApplicable(String uri) {
        return this.uri.matches(uri);
    }

    public HeaderAction getContentType() {
        List<Action> actions = server.getActions();
        for (Action action : actions) {
            if (action instanceof HeaderAction) {
                HeaderAction ha = (HeaderAction) action;
                if (HttpHeaders.CONTENT_TYPE.equalsIgnoreCase(ha.getHeader())) {
                    return ha;
                }
            }
        }
        return null;
    }

    @Override
    public abstract HttpResponse apply(HttpResponse input);

    public ToStringHelper objects() {
        return MoreObjects.toStringHelper(this).add("uri", uri);
    }

    @Override
    public String toString() {
        return objects().toString();
    }

    /**
     * StatusCodeAction.
     */
    public static class StatusCodeAction extends Action {

        private final int code;

        public StatusCodeAction(StubHttpServer server, Matcher<?> uri, int code) {
            super(server, uri);
            this.code = code;
        }

        @Override
        public HttpResponse apply(HttpResponse response) {
            Objects.requireNonNull(response);
            response.setStatus(HttpResponseStatus.valueOf(code));
            return response;
        }

        @Override
        public ToStringHelper objects() {
            return super.objects().add("code", code);
        }
    }

    /**
     * HeaderAction.
     */
    public static class HeaderAction extends Action {

        private final String header;

        private final String value;

        public HeaderAction(StubHttpServer server, Matcher<?> uri, String header, String value) {
            super(server, uri);
            this.header = Objects.requireNonNull(header);
            this.value = value;
        }

        public String getHeader() {
            return header;
        }

        public String getValue() {
            return value;
        }

        @Override
        public HttpResponse apply(HttpResponse response) {
            Objects.requireNonNull(response);
            response.headers().add(header, value);
            return response;
        }

        @Override
        public ToStringHelper objects() {
            return super.objects().add("header", header).add("value", value);
        }
    }

    /**
     * RawBodyAction.
     */
    public static class RawBodyAction extends Action {

        private final byte[] content;

        public RawBodyAction(StubHttpServer server, Matcher<?> uri, byte[] content) {
            super(server, uri);
            this.content = Objects.requireNonNull(content);
        }

        public byte[] getContent() {
            return content;
        }

        @Override
        public HttpResponse apply(HttpResponse response) {
            Objects.requireNonNull(response);
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(content.length);
            buffer.writeBytes(content);
            HttpResponse r
                    = new DefaultFullHttpResponse(response.getProtocolVersion(),
                            response.getStatus(), buffer);
            for (Map.Entry<String, String> entry : response.headers()) {
                r.headers().add(entry.getKey(), entry.getValue());
            }
            r.headers().add(HttpHeaders.CONTENT_LENGTH, content.length);
            return r;
        }

        @Override
        public ToStringHelper objects() {
            return super.objects().add("content", DatatypeConverter.printHexBinary(content));
        }
    }

    /**
     * BodyAction.
     */
    public static class BodyAction extends Action {

        private final Object content;

        public BodyAction(StubHttpServer server, Matcher<?> uri, Object content) {
            super(server, uri);
            this.content = Objects.requireNonNull(content);
        }

        public Object getContent() {
            return content;
        }

        @Override
        public HttpResponse apply(HttpResponse response) {
            Objects.requireNonNull(response);
            HeaderAction contentType = getContentType();
            if (contentType == null) {
                throw new RestMockException("No Content-Type found.");
            }
            ObjectSerializeContext ctx = new ObjectSerializeContext(content, contentType.getValue());
            ObjectSerializer os = ObjectSerializerStrategy.createObjectSerializer(ctx);
            try {
                return new RawBodyAction(server, uri, os.seriaize(ctx).getBytes(StandardCharsets.UTF_8)).apply(response);
            } catch (IOException ex) {
                throw new RestMockException("failed to serialize body.");
            }
        }

        @Override
        public ToStringHelper objects() {
            return super.objects().add("content", content.toString());
        }
    }
}
