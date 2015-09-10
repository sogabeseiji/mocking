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

import com.buildria.mocking.stub.Call;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// CHECKSTYLE:OFF
public abstract class RuleSpec {
// CHECKSTYLE:ON

    private final List<Rule> rules;

    public RuleSpec(List<Rule> rules) {
        this.rules = Objects.requireNonNull(rules);
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void validate(List<Call> calls) {
        List<Call> wrapped = new ArrayList<>(calls);
        Iterator<Call> it = wrapped.iterator();
        while (it.hasNext()) {
            Call call = it.next();
            LOG.debug("Call: {}", call.toString());
            for (Rule rule : rules) {
                if (!rule.apply(call)) {
                    LOG.debug("!!! unmatched: {}", rule.getDescription());
                    it.remove();
                    if (wrapped.isEmpty()) {
                        throw new AssertionError(rule.getDescription());
                    }
                    break;
                } else {
                    LOG.debug("*** matched: {}", rule.getDescription());
                }
            }
        }
    }

    private static final Logger LOG = LoggerFactory.getLogger(RuleSpec.class);
}
