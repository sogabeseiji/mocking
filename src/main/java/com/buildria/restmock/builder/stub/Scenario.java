package com.buildria.restmock.builder.stub;

import com.buildria.restmock.Function;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
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

}
