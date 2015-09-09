package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.stub.Call;
import com.buildria.mocking.stub.Pair;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;

public class NoHeaderRule extends Rule {

    private final String name;

    public NoHeaderRule(@Nonnull String name) {
        this.name = Objects.requireNonNull(name);
    }

    @Override
    public boolean apply(@Nonnull Call call) {
        Objects.requireNonNull(call);
        List<Pair> headers = call.getHeaders();
        Collection<Pair> matched = Collections2.filter(headers, new Predicate<Pair>() {
            @Override
            public boolean apply(Pair pair) {
                return name.equalsIgnoreCase(pair.getName());
            }
        });
        return matched.isEmpty();
    }

    @Override
    public String getDescription() {
        return String.format("(NoHeader) name: [%s]", name);
    }

}
