package com.buildria.restmock.builder.rule;

import com.buildria.restmock.builder.rule.Rule.RuleContext;
import com.buildria.restmock.stub.Call;
import com.google.common.base.Predicate;
import java.util.List;

// CHECKSTYLE:OFF
public abstract class Rule implements Predicate<RuleContext> {
// CHECKSTYLE:ON

    public static class RuleContext {

        private final Call call;

        private final List<Rule> rules;

        public RuleContext(Call call, List<Rule> rules) {
            this.call = call;
            this.rules = rules;
        }

        public Call getCall() {
            return call;
        }

        public List<Rule> getRules() {
            return rules;
        }

    }

    public Rule() {
        super();
    }

    @Override
    public abstract boolean apply(RuleContext ctx);

    public abstract String getDescription();

}
