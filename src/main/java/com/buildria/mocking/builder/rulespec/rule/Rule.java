package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.stub.Call;
import com.google.common.base.Predicate;

// CHECKSTYLE:OFF
public abstract class Rule implements Predicate<Call> {
// CHECKSTYLE:ON

    public Rule() {
        super();
    }

    @Override
    public abstract boolean apply(Call call);

    public abstract String getDescription();

}
