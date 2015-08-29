package com.buildria.restmock.builder.verify;

import com.buildria.restmock.builder.verify.Verifier.Method;
import com.buildria.restmock.stub.Call;
import com.buildria.restmock.stub.StubHttpServer;
import java.util.List;

public class MethodSpec {

    private final List<Call> calls;

    private MethodSpec(List<Call> cals) {
        this.calls = cals;
    }

    public static MethodSpec verify(StubHttpServer server) {
        return new MethodSpec(server.getCalls());
    }

    public static MethodSpec verify(List<Call> calls) {
        return new MethodSpec(calls);
    }

    private RequestSpec method(String method, String path) {
        List<Call> answers = Calls.filter(calls, new Method(method, path));
        if (answers.isEmpty()) {
            throw new AssertionError(
                    String.format("No calls found. method: %s path: %s", method, path));
        }
        return new RequestSpec(answers, path);
    }

    public RequestSpec get(String path) {
        return method("get", path);
    }

    public RequestSpec post(String path) {
        return method("post", path);
    }

    public RequestSpec put(String path) {
        return method("put", path);
    }

    public RequestSpec delete(String path) {
        return method("delete", path);
    }

}
