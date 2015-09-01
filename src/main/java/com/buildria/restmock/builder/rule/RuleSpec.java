package com.buildria.restmock.builder.rule;

import com.buildria.restmock.builder.rule.Rule.RuleContext;
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
        Iterator<Call> it = calls.iterator();
        while (it.hasNext()) {
            Call call = it.next();
            for (Rule rule : rules) {
                if (!rule.apply(new RuleContext(call, rules))) {
                    it.remove();
                    if (calls.isEmpty()) {
                        throw new AssertionError(rule.getDescription());
                    }
                    break;
                }
            }
        }
    }

}
