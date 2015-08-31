package com.buildria.restmock.builder.stub;

import java.util.ArrayList;
import java.util.List;

// CHECKSTYLE:OFF
public abstract class Spec {
// CHECKSTYLE:ON

    private final List<Action> actions = new ArrayList<>();

    public List<Action> getActions() {
        return actions;
    }


    public void addAction(Action action) {
        actions.add(action);
    }

}
