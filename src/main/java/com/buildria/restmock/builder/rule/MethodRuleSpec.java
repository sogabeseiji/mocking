package com.buildria.restmock.builder.rule;

import com.buildria.restmock.builder.rule.Rule.Method;

public class MethodRuleSpec extends RuleSpec {

    private static final MethodRuleSpec INSTANCE = new MethodRuleSpec();

    private MethodRuleSpec() {
        super();
    }

    private RequestRuleSpec method(String path, String method) {
        addRule(new Method(path, method));
        return new RequestRuleSpec();
    }

    public static RequestRuleSpec get(String path) {
        return INSTANCE.method(path, "get");
    }

    public static RequestRuleSpec post(String path) {
        return INSTANCE.method(path, "post");
    }

    public static RequestRuleSpec put(String path) {
        return INSTANCE.method(path, "put");
    }

    public static RequestRuleSpec delete(String path) {
        return INSTANCE.method(path, "delete");
    }

}
