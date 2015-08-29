package com.buildria.restmock.builder.stub;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.serialize.ObjectSerializer;
import com.buildria.restmock.serialize.ObjectSerializerContext;
import com.buildria.restmock.serialize.ObjectSerializerFactory;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.net.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.DatatypeConverter;
import org.hamcrest.Matcher;

// CHECKSTYLE:OFF
public abstract class Action {
// CHECKSTYLE:ON

    protected final Matcher<?> path;

    protected final StubHttpServer server;

    public Action(StubHttpServer server, Matcher<?> path) {
        this.server = Objects.requireNonNull(server);
        this.path = Objects.requireNonNull(path);
    }

    public Matcher<?> getPath() {
        return path;
    }

    public boolean isApplicable(String path) {
        return this.path.matches(path);
    }

    public HeaderAction getHeaderAction(String path, String headerName) {
        List<Action> actions = server.getActions();
        for (Action action : actions) {
            if (action.isApplicable(path) && action instanceof HeaderAction) {
                HeaderAction ha = (HeaderAction) action;
                if (ha.getHeader().equalsIgnoreCase(headerName)) {
                    return ha;
                }
            }
        }
        return null;
    }

    public abstract HttpResponse apply(HttpRequest req, HttpResponse res);

    public ToStringHelper objects() {
        return MoreObjects.toStringHelper(this).add("path", path);
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

        public StatusCodeAction(StubHttpServer server, Matcher<?> path, int code) {
            super(server, path);
            this.code = code;
        }

        @Override
        public HttpResponse apply(HttpRequest req, HttpResponse res) {
            Objects.requireNonNull(req);
            Objects.requireNonNull(res);
            res.setStatus(HttpResponseStatus.valueOf(code));
            return res;
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

        public HeaderAction(StubHttpServer server, Matcher<?> path, String header, String value) {
            super(server, path);
            this.header = Objects.requireNonNull(header);
            this.value = Objects.requireNonNull(value);
        }

        public String getHeader() {
            return header;
        }

        public String getValue() {
            return value;
        }

        @Override
        public HttpResponse apply(HttpRequest req, HttpResponse res) {
            Objects.requireNonNull(req);
            Objects.requireNonNull(res);
            res.headers().add(header, value);
            return res;
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

        public RawBodyAction(StubHttpServer server, Matcher<?> path, byte[] content) {
            super(server, path);
            this.content = Objects.requireNonNull(content);
        }

        public byte[] getContent() {
            return content;
        }

        @Override
        public HttpResponse apply(HttpRequest req, HttpResponse res) {
            Objects.requireNonNull(req);
            Objects.requireNonNull(res);
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(content.length);
            buffer.writeBytes(content);
            HttpResponse r
                    = new DefaultFullHttpResponse(res.getProtocolVersion(),
                            res.getStatus(), buffer);
            for (Map.Entry<String, String> entry : res.headers()) {
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

        public BodyAction(StubHttpServer server, Matcher<?> path, Object content) {
            super(server, path);
            this.content = Objects.requireNonNull(content);
        }

        public Object getContent() {
            return content;
        }

        @Override
        public HttpResponse apply(HttpRequest req, HttpResponse res) {
            Objects.requireNonNull(req);
            Objects.requireNonNull(res);
            HeaderAction contentType = getHeaderAction(req.getUri(), "Content-Type");
            if (contentType == null) {
                throw new RestMockException("No Content-Type found.");
            }
            ObjectSerializerContext ctx
                    = new ObjectSerializerContext(content, contentType.getValue());
            ObjectSerializer os = ObjectSerializerFactory.create(ctx);
            try {
                return new RawBodyAction(server, path,
                        os.serialize(ctx).getBytes(StandardCharsets.UTF_8)).apply(req, res);
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
