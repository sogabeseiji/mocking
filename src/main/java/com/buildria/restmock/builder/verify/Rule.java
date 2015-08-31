package com.buildria.restmock.builder.verify;

import com.buildria.restmock.stub.Call;
import com.buildria.restmock.stub.StubHttpServer;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

// CHECKSTYLE:OFF
public abstract class Rule implements Predicate<Call> {
// CHECKSTYLE:ON

    protected final StubHttpServer server;

    protected final String path;

    public Rule(@Nonnull StubHttpServer server, @Nonnull String path) {
        Objects.requireNonNull(server);
        Objects.requireNonNull(path);
        this.server = server;
        this.path = path;
    }

    @Nonnull
    public StubHttpServer getServer() {
        return server;
    }

    @Nonnull
    public String getPath() {
        return path;
    }

    @Override
    public abstract boolean apply(Call call);

    public static class Method extends Rule {

        private final String method;

        public Method(@Nonnull StubHttpServer server,
                @Nonnull String path, @Nonnull String method) {
            super(server, path);
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public boolean apply(@Nonnull Call call) {
            Objects.requireNonNull(call);
            return call.getPath().equalsIgnoreCase(path)
                    && method.equalsIgnoreCase(call.getMethod());
        }

    }

    public static class Header extends Rule {

        private final String name;

        private final Matcher<?> value;

        public Header(@Nonnull StubHttpServer server,  @Nonnull String path,
                @Nonnull String name, @Nonnull Matcher<?> value) {
            super(server, path);
            this.name = Objects.requireNonNull(name);
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public boolean apply(@Nonnull Call call) {
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

    public static class Parameter extends Rule {

        private final String key;

        private final String[] values;

        public Parameter(@Nonnull StubHttpServer server, @Nonnull String path,
                String key, @Nonnull String[] values) {
            super(server, path);
            this.key = Objects.requireNonNull(key);
            this.values = Objects.requireNonNull(values);
            Arrays.sort(this.values);
        }

        @Override
        public boolean apply(@Nonnull Call call) {
            Objects.requireNonNull(call);
            Map<String, List<String>> params = call.getParameters();
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
