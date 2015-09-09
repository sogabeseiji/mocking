/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buildria.mocking.builder.rulespec;

import com.buildria.mocking.builder.rulespec.rule.BodyRule;
import com.buildria.mocking.builder.rulespec.rule.HeaderRule;
import com.buildria.mocking.builder.rulespec.rule.ParameterRule;
import com.buildria.mocking.builder.rulespec.rule.Rule;
import java.util.List;
import org.hamcrest.Matcher;

import static com.buildria.mocking.http.MockingHttpHeaders.ACCEPT;
import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;
import static org.hamcrest.Matchers.equalTo;

public class RequestRuleSpec extends RuleSpec {

    RequestRuleSpec(List<Rule> rules) {
        super(rules);
    }

    public RequestRuleSpec withHeader(String name, Matcher<?> value) {
        addRule(new HeaderRule(name, value));
        return this;
    }

    public RequestRuleSpec withHeader(String name, String value) {
        return RequestRuleSpec.this.withHeader(name, equalTo(value));
    }

    public RequestRuleSpec withContentType(Matcher<?> value) {
        return RequestRuleSpec.this.withHeader(CONTENT_TYPE, value);
    }

    public RequestRuleSpec withContentType(String value) {
        return withHeader(CONTENT_TYPE, value);
    }

    public RequestRuleSpec withAccept(Matcher<?> value) {
        return RequestRuleSpec.this.withHeader(ACCEPT, value);
    }

    public RequestRuleSpec withAccept(String value) {
        return withHeader(ACCEPT, value);
    }

    public RequestRuleSpec withParameter(String key, String value) {
        return withParameters(key, new String[]{value});
    }

    public RequestRuleSpec withParameters(String key, String[] values) {
        addRule(new ParameterRule(key, values));
        return this;
    }

    public RequestRuleSpec withQueryParam(String key, String value) {
        return withParameter(key, value);
    }

    public RequestRuleSpec withQueryParams(String key, String... values) {
        return withParameters(key, values);
    }

    public RequestRuleSpec withBody(String path, Matcher<?> matcher) {
        addRule(new BodyRule(path, matcher));
        return this;
    }
}
