package com.buildria.restmock.builder.verify;

import com.buildria.restmock.Predicate;
import com.buildria.restmock.stub.Call;
import java.util.Objects;

public abstract class Verify implements Predicate<Call> {

    @Override
    public abstract boolean apply(Call call);

    public static abstract class Method extends Verify {

        private final String uri;

        public Method(String uri) {
            Objects.requireNonNull(uri);
            this.uri = uri;
        }

        public abstract String getMethod();

        @Override
        public boolean apply(Call call) {
            return call.getUri().equalsIgnoreCase(uri)
                    && getMethod().equalsIgnoreCase(call.getMethod());
        }

    }

    public static class Get extends Method {

        public Get(String uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return "get";
        }

    }

    public static class Post extends Method {

        public Post(String uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return "get";
        }

    }

    public static class Put extends Method {

        public Put(String uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return "get";
        }

    }

    public static class Delete extends Method {

        public Delete(String uri) {
            super(uri);
        }

        @Override
        public String getMethod() {
            return "get";
        }

    }

}
