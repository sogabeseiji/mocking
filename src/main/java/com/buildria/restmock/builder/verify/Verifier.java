package com.buildria.restmock.builder.verify;

import com.buildria.restmock.Predicate;
import com.buildria.restmock.stub.Call;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

// CHECKSTYLE:OFF
public abstract class Verifier implements Predicate<Call> {
// CHECKSTYLE:ON

    @Override
    public abstract boolean apply(Call call);

    public static class Method extends Verifier {

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

    public static class Header extends Verifier {

        private final String name;

        private final String value;

        public Header(String name, String value) {
            this.name = Objects.requireNonNull(name);
            this.value = Objects.requireNonNull(value);
        }

        // TODO
        @Override
        public boolean apply(Call call) {
            Map<String, String> headers = call.getHeaders();
            for (Entry<String, String> entry : headers.entrySet()) {
                String n = entry.getKey();
                String v = entry.getValue();
                if (name.equalsIgnoreCase(n) && value.equalsIgnoreCase(v)) {
                    return true;
                }
            }
            return false;
        }

    }

}
