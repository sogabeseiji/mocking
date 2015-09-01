package com.buildria.restmock.builder.rule;

import com.buildria.restmock.builder.rule.Rule.Method;

public class MethodRuleSpec extends RuleSpec {

    private MethodRuleSpec() {
        super();
    }

    private static RequestRuleSpec method(String path, String method) {
        RequestRuleSpec spec = new RequestRuleSpec();
        spec.addRule(new Method(path, method));
        return spec;
    }

    public static RequestRuleSpec get(String path) {
        return method(path, "get");
    }

    public static RequestRuleSpec post(String path) {
        return method(path, "post");
    }

    public static RequestRuleSpec put(String path) {
        return method(path, "put");
    }

    public static RequestRuleSpec delete(String path) {
        return method(path, "delete");
    }

}
