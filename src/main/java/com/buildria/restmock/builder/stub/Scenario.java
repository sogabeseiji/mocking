package com.buildria.restmock.builder.stub;

import com.buildria.restmock.Function;
import com.google.common.net.HttpHeaders;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
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

    public static Scenario status(Matcher<?> uri, final int code) {
        return new Scenario(uri) {
            @Override
            public HttpResponse apply(HttpResponse response) {
                response.setStatus(HttpResponseStatus.valueOf(code));
                return response;
            }
        };
    }

    public static Scenario header(Matcher<?> uri, final String header, final String value) {
        return new Scenario(uri) {
            @Override
            public HttpResponse apply(HttpResponse response) {
                response.headers().add(header, value);
                return response;
            }
        };
    }

    public static Scenario body(Matcher<?> uri, final byte[] content) {
        return new Scenario(uri) {
            @Override
            public HttpResponse apply(HttpResponse response) {
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
        };
    }
}
