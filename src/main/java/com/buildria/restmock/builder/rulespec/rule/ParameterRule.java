package com.buildria.restmock.builder.rulespec.rule;

import com.buildria.restmock.stub.Call;
import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

public class ParameterRule extends Rule {

    private final String key;

    private final String[] values;

    public ParameterRule(String key, @Nonnull String[] values) {
        super();
        this.key = Objects.requireNonNull(key);
        this.values = Objects.requireNonNull(values);
        Arrays.sort(this.values);
    }

    @Override
    public boolean apply(@Nonnull Call call) {
        Objects.requireNonNull(call);
        Map<String, List<String>> params = call.getParameters();
        List<String> vals = params.get(key);
        if (vals == null) {
            return false;
        }
        String[] sorted = vals.toArray(new String[0]);
        Arrays.sort(sorted);
        return Arrays.equals(sorted, values);
    }

    @Override
    public String getDescription() {
        return String.format("[Parameter] key: (%s) value: (%s)", key, Joiner.on(",").join(values));
    }

}
