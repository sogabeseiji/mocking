package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.stub.Call;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

/**
 *
 * @author sogabe
 */
public class HeaderRule extends Rule {

    private final String name;

    private final Matcher<?> value;

    public HeaderRule(@Nonnull String name, @Nonnull Matcher<?> value) {
        super();
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public boolean apply(@Nonnull Call call) {
        Objects.requireNonNull(call);
        Map<String, String> headers = call.getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String n = entry.getKey();
            String v = entry.getValue();
            if (name.equalsIgnoreCase(n) && value.matches(v)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return String.format("[Header] name: (%s) value: (%s)", name, value.toString());
    }

}