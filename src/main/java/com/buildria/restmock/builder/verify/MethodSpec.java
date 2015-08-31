package com.buildria.restmock.builder.verify;

import com.buildria.restmock.builder.verify.Rule.Method;
import com.buildria.restmock.stub.Call;
import com.buildria.restmock.stub.StubHttpServer;
import java.util.List;
import javax.annotation.Nonnull;

public class MethodSpec {

    private final StubHttpServer server;

    private MethodSpec(@Nonnull StubHttpServer server) {
        this.server = server;
    }

    public static MethodSpec verify(StubHttpServer server) {
        return new MethodSpec(server);
    }

    private RequestSpec method(String path, String method) {
        List<Call> answers = Calls.filter(server.getCalls(), new Method(server, path, method));
        if (answers.isEmpty()) {
            throw new AssertionError(
                    String.format("No calls found. path: %s method: %s ", path, method));
        }
        return new RequestSpec(server, answers, path);
    }

    public RequestSpec get(String path) {
        return method(path, "get");
    }

    public RequestSpec post(String path) {
        return method(path, "post");
    }

    public RequestSpec put(String path) {
        return method(path, "put");
    }

    public RequestSpec delete(String path) {
        return method(path, "delete");
    }

}
