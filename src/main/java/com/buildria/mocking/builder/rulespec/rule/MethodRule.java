package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.stub.Call;
import java.util.Objects;
import javax.annotation.Nonnull;

public class MethodRule extends Rule {

    private final String path;

    private final String method;

    public MethodRule(@Nonnull String path, @Nonnull String method) {
        super();
        this.path = Objects.requireNonNull(path);
        this.method = Objects.requireNonNull(method);
    }

    @Override
    public boolean apply(@Nonnull Call call) {
        Objects.requireNonNull(call);
        return call.getPath().equalsIgnoreCase(path)
                && method.equalsIgnoreCase(call.getMethod());
    }

    @Override
    public String getDescription() {
        return String.format("[Method] path: (%s) method: (%s)", path, method);
    }

}