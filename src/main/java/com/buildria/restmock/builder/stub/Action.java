package com.buildria.restmock.builder.stub;

import com.buildria.restmock.Function;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import com.google.common.net.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Map;
import javax.xml.bind.DatatypeConverter;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class Action implements Function<HttpResponse, HttpResponse> {

    protected final Matcher<?> uri;

    protected final StubHttpServer server;

    public Action(StubHttpServer server, Matcher<?> uri) {
        this.server = server;
        this.uri = uri;
    }

    public Matcher<?> getUri() {
        return uri;
    }

    public boolean isApplicable(String uri) {
        return this.uri.matches(uri);
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
            this.header = header;
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
            response.headers().add(header, value);
            return response;
        }

        @Override
        public ToStringHelper objects() {
            return super.objects().add("header", header).add("value", value);
        }
    }

    /**
     * BodyAction.
     */
    public static class BodyAction extends Action {

        private final byte[] content;

        public BodyAction(StubHttpServer server, Matcher<?> uri, byte[] content) {
            super(server, uri);
            this.content = content;
        }

        public byte[] getContent() {
            return content;
        }

        @Override
        public HttpResponse apply(HttpResponse response) {
            byte[] body = (byte[]) content;
            ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(body.length);
            buffer.writeBytes(body);
            HttpResponse r
                    = new DefaultFullHttpResponse(response.getProtocolVersion(),
                            response.getStatus(), buffer);
            for (Map.Entry<String, String> entry : response.headers()) {
                r.headers().add(entry.getKey(), entry.getValue());
            }
            r.headers().add(HttpHeaders.CONTENT_LENGTH, body.length);
            return r;
        }

        @Override
        public ToStringHelper objects() {
            return super.objects().add("content", DatatypeConverter.printHexBinary(content));
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(Action.class);
}
