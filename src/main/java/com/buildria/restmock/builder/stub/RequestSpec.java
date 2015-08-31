package com.buildria.restmock.builder.stub;

import java.util.Objects;
import org.hamcrest.Matcher;

import static org.hamcrest.Matchers.equalTo;

/**
 *
 * @author sogabe
 */
public class RequestSpec {

    private final Matcher<?>  path;

    private RequestSpec(Matcher<?> path) {
        this.path = path;
    }

    public static RequestSpec path(String path) {
        return path(equalTo(Objects.requireNonNull(path)));
    }

    public static RequestSpec path(Matcher<?> path) {
        return new RequestSpec(path);
    }

    public ResponseSpec then() {
        return new ResponseSpec(path);
    }

}
