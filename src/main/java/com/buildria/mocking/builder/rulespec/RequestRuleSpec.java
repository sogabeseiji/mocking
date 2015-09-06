package com.buildria.mocking.builder.rulespec;

import com.buildria.mocking.builder.rulespec.rule.ParameterRule;
import com.buildria.mocking.builder.rulespec.rule.HeaderRule;
import com.buildria.mocking.builder.rulespec.rule.BodyRule;
import org.hamcrest.Matcher;

import static com.buildria.mocking.http.MockingHttpHeaders.ACCEPT;
import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.equalTo;

public class RequestRuleSpec extends RuleSpec {

    RequestRuleSpec() {
        super();
    }

    public RequestRuleSpec header(String name, Matcher<?> value) {
        addRule(new HeaderRule(name, value));
        return this;
    }

    public RequestRuleSpec header(String name, String value) {
        return header(name, equalTo(value));
    }

    public RequestRuleSpec contentType(Matcher<?> value) {
        return header(CONTENT_TYPE, value);
    }

    public RequestRuleSpec contentType(String value) {
        return header(CONTENT_TYPE, value);
    }

    public RequestRuleSpec accept(Matcher<?> value) {
        return header(ACCEPT, value);
    }

    public RequestRuleSpec accept(String value) {
        return header(ACCEPT, value);
    }

    public RequestRuleSpec parameter(String key, String value) {
        return parameters(key, new String[]{value});
    }

    public RequestRuleSpec parameters(String key, String[] values) {
        addRule(new ParameterRule(key, values));
        return this;
    }

    public RequestRuleSpec queryParam(String key, String value) {
        return parameter(key, value);
    }

    public RequestRuleSpec queryParams(String key, String... values) {
        return parameters(key, values);
    }

    public RequestRuleSpec body(String path, Matcher<?> matcher) {
        addRule(new BodyRule(path, matcher));
        return this;
    }
}
