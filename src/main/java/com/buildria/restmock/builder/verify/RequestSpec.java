package com.buildria.restmock.builder.verify;

import com.buildria.restmock.stub.Call;
import java.util.List;

public class RequestSpec {

    private final List<Call> calls;

    private final String uri;

    RequestSpec(List<Call> calls, String uri) {
        this.calls = calls;
        this.uri = uri;
    }


}
