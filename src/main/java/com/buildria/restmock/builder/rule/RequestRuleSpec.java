package com.buildria.restmock.builder.rule;

import com.google.common.net.MediaType;
import org.hamcrest.Matcher;

import static com.buildria.restmock.http.RMHttpHeaders.ACCEPT;
import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;
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

    public RequestRuleSpec header(String name, MediaType value) {
        return header(name, value.toString());
    }

    public RequestRuleSpec contentType(Matcher<?> value) {
        return header(CONTENT_TYPE, value);
    }

    public RequestRuleSpec contentType(String value) {
        return header(CONTENT_TYPE, value);
    }

    public RequestRuleSpec contentType(MediaType value) {
        return header(CONTENT_TYPE, value);
    }

    public RequestRuleSpec accept(Matcher<?> value) {
        return header(ACCEPT, value);
    }

    public RequestRuleSpec accept(String value) {
        return header(ACCEPT, value);
    }

    public RequestRuleSpec accept(MediaType value) {
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
