package com.buildria.restmock.builder.stub;

import java.util.ArrayList;
import java.util.List;

public abstract class Spec {

    private final List<Action> actions = new ArrayList<>();

    public List<Action> getActions() {
        return actions;
    }


    public void addAction(Action action) {
        actions.add(action);
    }

}
