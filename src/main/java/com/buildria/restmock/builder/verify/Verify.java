package com.buildria.restmock.builder.verify;

import com.buildria.restmock.Predicate;
import com.buildria.restmock.stub.Call;
import java.util.Objects;

// CHECKSTYLE:OFF
public abstract class Verify implements Predicate<Call> {
// CHECKSTYLE:ON

    @Override
    public abstract boolean apply(Call call);

    public static class Method extends Verify {

        private final String uri;

        private final String method;

        public Method(String uri, String method) {
            Objects.requireNonNull(uri);
            Objects.requireNonNull(method);
            this.uri = uri;
            this.method = method;
        }

        @Override
        public boolean apply(Call call) {
            return call.getUri().equalsIgnoreCase(uri)
                    && method.equalsIgnoreCase(call.getMethod());
        }

    }
}
