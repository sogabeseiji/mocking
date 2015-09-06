package com.buildria.mocking.builder.actionspec;

import com.buildria.mocking.builder.actionspec.action.Action;
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