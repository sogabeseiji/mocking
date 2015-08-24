package com.buildria.restmock.builder.stub;

import com.buildria.restmock.Function;
import com.google.common.net.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.nio.charset.Charset;
import java.util.Map;
import org.hamcrest.Matcher;

/**
 *
 * @author sogabe
 */
public abstract class Scenario implements Function<HttpResponse, HttpResponse> {

    protected final Matcher<?> uri;

    public Scenario(Matcher<?> uri) {
        this.uri = uri;
    }

    public boolean isApplicable(String uri) {
        return this.uri.matches(uri);
    }

    @Override
    public abstract HttpResponse apply(HttpResponse input);

    public static class Status extends Scenario {

        private final int code;

        public Status(Matcher<?> uri, int code) {
            super(uri);
            this.code = code;
        }

        @Override
        public HttpResponse apply(HttpResponse response) {
            response.setStatus(HttpResponseStatus.valueOf(code));
            return response;
        }
    }

    public static class Header extends Scenario {

        private final String header;

        private final String value;

        public Header(Matcher<?> uri, String header, String value) {
            super(uri);
            this.header = header;
            this.value = value;
        }

        @Override
        public HttpResponse apply(HttpResponse response) {
            response.headers().add(header, value);
            return response;
        }

    }

    public static class Body extends Scenario {

        private final byte[] body;

        public Body(Matcher<?> uri, String content, Charset charset) {
            super(uri);
            this.body = content.getBytes(charset);
        }

        public Body(Matcher<?> uri, byte[] content) {
            super(uri);
            this.body = content;
        }

        @Override
        public HttpResponse apply(HttpResponse response) {
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

    }
}
