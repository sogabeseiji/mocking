package com.buildria.restmock.builder.rule;

import com.buildria.restmock.stub.Call;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

// CHECKSTYLE:OFF
public abstract class RuleSpec {
// CHECKSTYLE:ON

    private final List<Rule> rules = new ArrayList<>();

    public List<Rule> getRules() {
        return rules;
    }

    public void addRule(Rule rule) {
        rules.add(rule);
    }

    public void validate(List<Call> calls) {
        List<Call> wrapped = new ArrayList<>(calls);
        Iterator<Call> it = wrapped.iterator();
        while (it.hasNext()) {
            Call call = it.next();
            for (Rule rule : rules) {
                if (!rule.apply(call)) {
                    it.remove();
                    if (wrapped.isEmpty()) {
                        throw new AssertionError(rule.getDescription());
                    }
                    break;
                }
            }
        }
    }

}
