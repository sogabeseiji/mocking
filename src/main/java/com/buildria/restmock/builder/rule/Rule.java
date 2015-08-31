package com.buildria.restmock.builder.rule;

import com.buildria.restmock.builder.rule.Rule.RuleContext;
import com.buildria.restmock.stub.Call;
import com.google.common.base.Predicate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

// CHECKSTYLE:OFF
public abstract class Rule implements Predicate<RuleContext> {
// CHECKSTYLE:ON

    public static class RuleContext {

        public Call call;
        public List<Rule> rules;

        public RuleContext(Call call, List<Rule> rules) {
            this.call = call;
            this.rules = rules;
        }
    }

    protected final String path;

    public Rule(@Nonnull String path) {
        Objects.requireNonNull(path);
        this.path = path;
    }

    @Nonnull
    public String getPath() {
        return path;
    }

    @Override
    public abstract boolean apply(RuleContext ctx);

    public static class Method extends Rule {

        private final String method;

        public Method(@Nonnull String path, @Nonnull String method) {
            super(path);
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public boolean apply(@Nonnull RuleContext ctx) {
            Objects.requireNonNull(ctx);
            Call call = ctx.call;
            return call.getPath().equalsIgnoreCase(path)
                    && method.equalsIgnoreCase(call.getMethod());
        }

    }

    public static class Header extends Rule {

        private final String name;

        private final Matcher<?> value;

        public Header(@Nonnull String path,
                @Nonnull String name, @Nonnull Matcher<?> value) {
            super(path);
            this.name = Objects.requireNonNull(name);
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public boolean apply(@Nonnull RuleContext ctx) {
            Objects.requireNonNull(ctx);
            Call call = ctx.call;
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

        public Parameter(@Nonnull String path, String key, @Nonnull String[] values) {
            super(path);
            this.key = Objects.requireNonNull(key);
            this.values = Objects.requireNonNull(values);
            Arrays.sort(this.values);
        }

        @Override
        public boolean apply(@Nonnull RuleContext ctx) {
            Objects.requireNonNull(ctx);
            Call call = ctx.call;
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
