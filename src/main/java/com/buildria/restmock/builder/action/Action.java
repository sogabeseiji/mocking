package com.buildria.restmock.builder.action;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.serializer.ObjectSerializer;
import com.buildria.restmock.serializer.ObjectSerializerContext;
import com.buildria.restmock.serializer.ObjectSerializerFactory;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.DatatypeConverter;
import org.hamcrest.Matcher;

import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_LENGTH;
import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;

// CHECKSTYLE:OFF
public abstract class Action {
// CHECKSTYLE:ON

    private final Matcher<?> path;

    public Action(@Nonnull Matcher<?> path) {
        this.path = Objects.requireNonNull(path);
    }

    @Nonnull
    public Matcher<?> getPath() {
        return path;
    }

    public boolean isApplicable(String path) {
        return this.path.matches(path);
    }

    @Nullable
    public Header getHeader(String path, String headerName, List<Action> actions) {
        for (Action action : actions) {
            if (action.isApplicable(path) && action instanceof Header) {
                Header ha = (Header) action;
                if (ha.getHeader().equalsIgnoreCase(headerName)) {
                    return ha;
                }
            }
        }
        return null;
    }

    @Nonnull
    public abstract HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res);

    public ToStringHelper objects() {
        return MoreObjects.toStringHelper(this).add("path", path);
    }

    @Override
    public String toString() {
        return objects().toString();
    }

    /**
     * StatusCode.
     */
    public static class StatusCode extends Action {

        private final int code;

        public StatusCode(@Nonnull Matcher<?> path, int code) {
            super(path);
            this.code = code;
        }

        @Nonnull
        @Override
        public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
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
     * Header.
     */
    public static class Header extends Action {

        private final String header;

        private final String value;

        @Nonnull
        public Header(@Nonnull Matcher<?> path,
                @Nonnull String header, @Nonnull String value) {
            super(path);
            this.header = Objects.requireNonNull(header);
            this.value = Objects.requireNonNull(value);
        }

        @Nonnull
        public String getHeader() {
            return header;
        }

        @Nonnull
        public String getValue() {
            return value;
        }

        @Nonnull
        @Override
        public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
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
     * RawBody.
     */
    public static class RawBody extends Action {

        private final byte[] content;

        public RawBody(@Nonnull Matcher<?> path, @Nonnull byte[] content) {
            super(path);
            this.content = Objects.requireNonNull(content);
        }

        public byte[] getContent() {
            return content;
        }

        @Nonnull
        @Override
        public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
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
            r.headers().add(CONTENT_LENGTH, content.length);
            return r;
        }

        @Override
        public ToStringHelper objects() {
            return super.objects().add("content", DatatypeConverter.printHexBinary(content));
        }
    }

    /**
     * Body.
     */
    public static class Body extends Action {

        private final Object content;

        private final List<Action> actions;

        public Body(@Nonnull Matcher<?> path, @Nonnull Object content,
                @Nonnull List<Action> actions) {
            super(path);
            this.content = Objects.requireNonNull(content);
            this.actions = Objects.requireNonNull(actions);
        }

        public Object getContent() {
            return content;
        }

        @Nonnull
        @Override
        public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
            Objects.requireNonNull(req);
            Objects.requireNonNull(res);
            Header contentType = getHeader(req.getUri(), CONTENT_TYPE, actions);
            if (contentType == null) {
                throw new RestMockException("No Content-Type found.");
            }
            ObjectSerializerContext ctx
                    = new ObjectSerializerContext(content, contentType.getValue());
            ObjectSerializer os = ObjectSerializerFactory.create(ctx);
            try {
                return new RawBody(
                        getPath(),
                        os.serialize(ctx).getBytes(StandardCharsets.UTF_8)).
                        apply(req, res);
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
