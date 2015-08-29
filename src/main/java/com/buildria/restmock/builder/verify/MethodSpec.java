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

    public RequestSpec get(String path) {
        List<Call> answers = CallsVerifier.verify(calls, new Method("get", path));
        return new RequestSpec(answers, path);
    }

    public RequestSpec post(String path) {
        List<Call> answers = CallsVerifier.verify(calls, new Method("post", path));
        return new RequestSpec(answers, path);
    }

    public RequestSpec put(String path) {
        List<Call> answers = CallsVerifier.verify(calls, new Method("put", path));
        return new RequestSpec(answers, path);
    }

    public RequestSpec delete(String path) {
        List<Call> answers = CallsVerifier.verify(calls, new Method("delete", path));
        return new RequestSpec(answers, path);
    }

}
