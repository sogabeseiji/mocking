package com.buildria.restmock.builder.verify;

import com.buildria.restmock.Predicate;
import com.buildria.restmock.stub.Call;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VerifyCalls {

    private VerifyCalls() {
        super();
    }

    public static List<Call> apply(List<Call> calls, Predicate<Call> predicates) {
        Objects.requireNonNull(calls);
        Objects.requireNonNull(predicates);

        List<Call> answers = new ArrayList<>();
        for (Call call : calls) {
            if (predicates.apply(call)) {
                answers.add(call);
            }
        }

        if (answers.isEmpty()) {
            throw new AssertionError();
        }

        return answers;
    }
}
