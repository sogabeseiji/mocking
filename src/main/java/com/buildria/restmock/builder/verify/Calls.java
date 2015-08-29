package com.buildria.restmock.builder.verify;

import com.buildria.restmock.stub.Call;
import com.google.common.base.Predicate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Calls {

    private Calls() {
        super();
    }

    public static List<Call> filter(List<Call> calls, Predicate<Call> predicates) {
        Objects.requireNonNull(calls);
        Objects.requireNonNull(predicates);

        List<Call> answers = new ArrayList<>();
        for (Call call : calls) {
            if (predicates.apply(call)) {
                answers.add(call);
            }
        }

        return answers;
    }
}
