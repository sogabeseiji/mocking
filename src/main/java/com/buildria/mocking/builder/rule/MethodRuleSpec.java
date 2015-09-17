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
package com.buildria.mocking.builder.rule;

import com.buildria.mocking.Mocking;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.text.StrSubstitutor;

public class MethodRuleSpec extends RuleSpec {

    private String path;

    private final String method;

    private MethodRuleSpec(String path, String method) {
        super(Mocking.HOLDER.get().getCalls());
        this.path = Objects.requireNonNull(path);
        this.method = Objects.requireNonNull(method);
    }

    private static MethodRuleSpec method(String path, String method) {
        return new MethodRuleSpec(path, method);
    }

    public static MethodRuleSpec get(String path) {
        return method(path, "get");
    }

    public static MethodRuleSpec post(String path) {
        return method(path, "post");
    }

    public static MethodRuleSpec put(String path) {
        return method(path, "put");
    }

    public static MethodRuleSpec delete(String path) {
        return method(path, "delete");
    }

    public MethodRuleSpec withPathParam(String name, Object value) {
        Map<String, String> map = new HashMap<>();
        map.put(name, String.valueOf(value));
        this.path = StrSubstitutor.replace(path, map, "{", "}");
        return this;
    }

    public RequestRuleSpec then() {
        validate(new MethodRule(path, method));
        return new RequestRuleSpec(getCalls());
    }
}
