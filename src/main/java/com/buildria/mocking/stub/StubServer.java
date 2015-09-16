package com.buildria.mocking.stub;

import com.buildria.mocking.MockingException;
import com.buildria.mocking.builder.action.Action;
import java.util.List;

public interface StubServer {

    void addAction(Action action);

    void addActions(List<Action> actions);

    List<Action> getActions();

    List<Call> getCalls();

    StubServer start() throws MockingException;

    void stop();

}
