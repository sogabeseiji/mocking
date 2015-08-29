package com.buildria.restmock.builder.verify;

import com.buildria.restmock.stub.Call;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import org.hamcrest.Matcher;

// CHECKSTYLE:OFF
public abstract class Verifier implements Predicate<Call> {
// CHECKSTYLE:ON

    @Override
    public abstract boolean apply(Call call);

    public static class Method extends Verifier {

        private final String path;

        private final String method;

        public Method(String method, String path) {
            Objects.requireNonNull(path);
            Objects.requireNonNull(method);
            this.path = path;
            this.method = method;
        }

        @Override
        public boolean apply(Call call) {
            Objects.requireNonNull(call);
            return call.getPath().equalsIgnoreCase(path)
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

        public Parameter(String key, String... values) {
            this.key = Objects.requireNonNull(key);
            this.values = Objects.requireNonNull(values);
            Arrays.sort(this.values);
        }

        @Override
        public boolean apply(Call call) {
            Objects.requireNonNull(call);
            Map<String, List<String>> params = call.getParameters();
            if (params == null) {
                params = Collections.emptyMap();
            }
            List<String> vals = params.get(key);
            if (vals == null) {
                return false;
            }

            String[] sorted = vals.toArray(new String[0]);
            Arrays.sort(sorted);
            return Arrays.equals(sorted, values);
        }

    }

}

