package com.buildria.restmock.builder.verify;

import com.buildria.restmock.stub.Call;
import com.google.common.base.Predicate;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

// CHECKSTYLE:OFF
public abstract class Verifier implements Predicate<Call> {
// CHECKSTYLE:ON

    @Override
    public abstract boolean apply(Call call);

    public static class Method extends Verifier {

        private final String uri;

        private final String method;

        public Method(String method, String uri) {
            Objects.requireNonNull(uri);
            Objects.requireNonNull(method);
            this.uri = uri;
            this.method = method;
        }

        @Override
        public boolean apply(Call call) {
            Objects.requireNonNull(call);
            return call.getUri().equalsIgnoreCase(uri)
                    && method.equalsIgnoreCase(call.getMethod());
        }

    }

    public static class Header extends Verifier {

        private final String name;

        private final Matcher<?> value;

        public Header(String name, Matcher<?> value) {
            this.name = Objects.requireNonNull(name);
            this.value = Objects.requireNonNull(value);
        }

        // TODO
        @Override
        public boolean apply(Call call) {
            Objects.requireNonNull(call);
            Map<String, String> headers = call.getHeaders();
            for (Entry<String, String> entry : headers.entrySet()) {
                String n = entry.getKey();
                String v = entry.getValue();
                if (name.equalsIgnoreCase(n) && value.matches(v)) {
                    return true;
                }
            }
            return false;
        }

    }

    public static class Parameter extends Verifier {

        private final String key;

        private final String[] values;

        public Parameter(String key, String value) {
            this(key, new String[] { value });
        }

        public Parameter(String key, String... values) {
            Objects.requireNonNull(key);
            Objects.requireNonNull(values);
            this.key = key;
            this.values = values;
        }

        @Override
        public boolean apply(Call call) {
            Objects.requireNonNull(call);
            Map<String, List<String>> params = call.getParameters();
            List<String> vals = params.get(key);
            if (vals == null) {
                return false;
            }

            return Matchers.containsInAnyOrder(vals.toArray()).matches(values);
        }

    }

}

