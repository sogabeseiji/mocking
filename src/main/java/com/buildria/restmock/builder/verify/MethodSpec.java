package com.buildria.restmock.builder.verify;

import com.buildria.restmock.builder.verify.Verify.Method;
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

    public RequestSpec get(String uri) {
        List<Call> answers = VerifyCalls.apply(calls, new Method(uri, "get"));
        return new RequestSpec(answers, uri);
    }

    public RequestSpec post(String uri) {
        List<Call> answers = VerifyCalls.apply(calls, new Method(uri, "post"));
        return new RequestSpec(answers, uri);
    }

    public RequestSpec put(String uri) {
        List<Call> answers = VerifyCalls.apply(calls, new Method(uri, "put"));
        return new RequestSpec(answers, uri);
    }

    public RequestSpec delete(String uri) {
        List<Call> answers = VerifyCalls.apply(calls, new Method(uri, "delete"));
        return new RequestSpec(answers, uri);
    }

}
