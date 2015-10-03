package com.buildria.mocking.builder.rule;

import com.buildria.mocking.stub.Call;
import java.util.Objects;

public class NoBodyRule implements Rule {

    @Override
    public boolean apply(Call call) {
        Objects.requireNonNull(call);
        byte[] body = call.getBody();
        return body == null || body.length == 0;
    }

    @Override
    public String getDescription() {
        return String.format("(NoBody)");
    }

}
