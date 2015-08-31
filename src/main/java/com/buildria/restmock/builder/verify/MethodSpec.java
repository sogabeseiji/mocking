package com.buildria.restmock.builder.verify;

import com.buildria.restmock.builder.verify.Rule.Method;

public class MethodSpec extends Spec {

    private static final MethodSpec instance = new MethodSpec();

    private MethodSpec() {
        super();
    }

    private RequestSpec method(String path, String method) {
        addRule(new Method(path, method));
        return new RequestSpec(path);
    }

    public static RequestSpec get(String path) {
        return instance.method(path, "get");
    }

    public static RequestSpec post(String path) {
        return instance.method(path, "post");
    }

    public static RequestSpec put(String path) {
        return instance.method(path, "put");
    }

    public static RequestSpec delete(String path) {
        return instance.method(path, "delete");
    }

}
