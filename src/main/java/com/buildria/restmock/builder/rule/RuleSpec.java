package com.buildria.restmock.builder.rule;

import com.buildria.restmock.stub.Call;
import java.util.ArrayList;
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
        List<Call> validated = new ArrayList<>();
        for (Call call : calls) {
            boolean success = true;
            for (Rule rule : rules) {
                if (!rule.apply(new Rule.RuleContext(call, rules))) {
                    success= false;
                    break;
                }
            }
            if (success) {
                validated.add(call);
            }
        }
        if (validated.isEmpty()) {
            throw new AssertionError("No calls found.");
        }
    }

}
