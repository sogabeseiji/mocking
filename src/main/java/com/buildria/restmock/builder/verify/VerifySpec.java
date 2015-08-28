package com.buildria.restmock.builder.verify;

import com.buildria.restmock.builder.verify.Verify.Delete;
import com.buildria.restmock.builder.verify.Verify.Get;
import com.buildria.restmock.builder.verify.Verify.Post;
import com.buildria.restmock.builder.verify.Verify.Put;
import com.buildria.restmock.stub.Call;
import com.buildria.restmock.stub.StubHttpServer;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VerifySpec {

    private final List<Call> calls;

    private VerifySpec(List<Call> cals) {
        this.calls = cals;
    }

    public static VerifySpec verify(StubHttpServer server) {
        return new VerifySpec(server.getCalls());
    }

    public static VerifySpec verify(List<Call> calls) {
        return new VerifySpec(calls);
    }

    public RequestSpec get(String uri) {
        return new RequestSpec(VerifyCalls.apply(calls, new Get(uri)), uri);
    }

    public RequestSpec post(String uri) {
        return new RequestSpec(VerifyCalls.apply(calls, new Post(uri)), uri);
    }

    public RequestSpec put(String uri) {
        return new RequestSpec(VerifyCalls.apply(calls, new Put(uri)), uri);
    }

    public RequestSpec delete(String uri) {
        return new RequestSpec(VerifyCalls.apply(calls, new Delete(uri)), uri);
    }

    private static final Logger LOG = LoggerFactory.getLogger(VerifySpec.class);
}
