package com.buildria.restmock.builder.action;

import java.util.Objects;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

public class RequestActionSpec extends ActionSpec {

    private final Matcher<?>  path;

    private RequestActionSpec(Matcher<?> path) {
        this.path = path;
    }

    public static RequestActionSpec when(String path) {
        return when(equalTo(Objects.requireNonNull(path)));
    }

    public static RequestActionSpec when(Matcher<?> path) {
        return new RequestActionSpec(path);
    }

    public ResponseActionSpec then() {
        return new ResponseActionSpec(path);
    }

}
