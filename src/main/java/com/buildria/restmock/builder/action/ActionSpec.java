package com.buildria.restmock.builder.action;

import java.util.ArrayList;
import java.util.List;

// CHECKSTYLE:OFF
public abstract class ActionSpec {
// CHECKSTYLE:ON

    private final List<Action> actions = new ArrayList<>();

    public List<Action> getActions() {
        return actions;
    }


    public void addAction(Action action) {
        actions.add(action);
    }

}
