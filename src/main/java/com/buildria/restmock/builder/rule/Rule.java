package com.buildria.restmock.builder.rule;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.builder.rule.Rule.RuleContext;
import com.buildria.restmock.stub.Call;
import com.google.common.base.Joiner;
import com.google.common.base.Predicate;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;

// CHECKSTYLE:OFF
public abstract class Rule implements Predicate<RuleContext> {
// CHECKSTYLE:ON

    public static class RuleContext {

        private final Call call;

        private final List<Rule> rules;

        public RuleContext(Call call, List<Rule> rules) {
            this.call = call;
            this.rules = rules;
        }

        public Call getCall() {
            return call;
        }

        public List<Rule> getRules() {
            return rules;
        }

    }

    public Rule() {
        super();
    }

    @Override
    public abstract boolean apply(RuleContext ctx);

    public abstract String getDescription();

    public static class Method extends Rule {

        private final String path;

        private final String method;

        public Method(@Nonnull String path, @Nonnull String method) {
            super();
            this.path = Objects.requireNonNull(path);
            this.method = Objects.requireNonNull(method);
        }

        @Override
        public boolean apply(@Nonnull RuleContext ctx) {
            Objects.requireNonNull(ctx);
            Call call = ctx.getCall();
            return call.getPath().equalsIgnoreCase(path)
                    && method.equalsIgnoreCase(call.getMethod());
        }

        @Override
        public String getDescription() {
            return String.format("[Method] path: (%s) method: (%s)",
                    path, method);
        }
    }

    public static class Header extends Rule {

        private final String name;

        private final Matcher<?> value;

        public Header(@Nonnull String name, @Nonnull Matcher<?> value) {
            super();
            this.name = Objects.requireNonNull(name);
            this.value = Objects.requireNonNull(value);
        }

        @Override
        public boolean apply(@Nonnull RuleContext ctx) {
            Objects.requireNonNull(ctx);
            Call call = ctx.getCall();
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

        @Override
        public String getDescription() {
            return String.format("[Header] name: (%s) value: (%s)",
                    name, value.toString());
        }

    }

    public static class Parameter extends Rule {

        private final String key;

        private final String[] values;

        public Parameter(String key, @Nonnull String[] values) {
            super();
            this.key = Objects.requireNonNull(key);
            this.values = Objects.requireNonNull(values);
            Arrays.sort(this.values);
        }

        @Override
        public boolean apply(@Nonnull RuleContext ctx) {
            Objects.requireNonNull(ctx);
            Call call = ctx.getCall();
            Map<String, List<String>> params = call.getParameters();
            List<String> vals = params.get(key);
            if (vals == null) {
                return false;
            }

            String[] sorted = vals.toArray(new String[0]);
            Arrays.sort(sorted);
            return Arrays.equals(sorted, values);
        }

        @Override
        public String getDescription() {
            return String.format("[Parameter] key: (%s) value: (%s)",
                    key, Joiner.on(",").join(values));
        }
    }

    public static class Body extends Rule {

        // TODO
        private enum Type {

            XML("xml"), JSON("json"), OTHER("other");

            private final String name;

            private Type(String name) {
                this.name = name;
            }

            public boolean matches(String contentType) {
                if (contentType == null) {
                    return false;
                }
                return contentType.contains(name);
            }
        }

        private final String path;

        private final Matcher<?> matcher;

        public Body(String path, Matcher<?> matcher) {
            super();
            this.path = Objects.requireNonNull(path);
            this.matcher = Objects.requireNonNull(matcher);
        }

        @Override
        public boolean apply(@Nonnull RuleContext ctx) {
            Objects.requireNonNull(ctx);
            Call call = ctx.getCall();

            // TODO
            byte[] body = call.getBody();
            if (body == null || body.length == 0) {
                return matcher.matches(body);
            }

            String content = new String(body, StandardCharsets.UTF_8);
            String ContentType = call.getHeaders().get(CONTENT_TYPE);
            Object obj = convertContent(ContentType, content, path);

            return matcher.matches(obj);
        }

        private Object convertContent(String contentType, String content, String path) {
            if (Type.XML.matches(contentType)) {
                return  new XmlPath(content).get(path);
            } else if (Type.JSON.matches(contentType)) {
                return  new JsonPath(content).get(path);
            }
            throw new RestMockException("Not supported Content-Type.");
        }

        @Override
        public String getDescription() {
            return String.format("[Body] path: (%s) matcher: (%s)",
                    path, matcher.toString());
        }

    }
}
