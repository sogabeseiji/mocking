package com.buildria.restmock.stub;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.xml.bind.DatatypeConverter;

public class Call {

    private String uri;

    private String method;

    private final Map<String, String> headers = new HashMap<>();

    private byte[] body;

    private Call() {
        //
    }

    public static Call fromRequest(HttpRequest req) {
        Objects.requireNonNull(req);
        Call call = new Call();
        call.uri = req.getUri();
        call.method = req.getMethod().name();
        for (Map.Entry<String, String> entry : req.headers().entries()) {
            call.headers.put(entry.getKey(), entry.getValue());
        }
        if (req instanceof HttpContent) {
            ByteBuf buf = ((HttpContent) req).content();
            if (buf != null) {
                call.body = buf.copy().array();
            }
        }

        return call;
    }

    public String getUri() {
        return uri;
    }

    public String getMethod() {
        return method;
    }

    public Map<String, String> getHeaders() {
        return headers;
    }

    public byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("uri", uri).add("method", method).add("headers", headers).
                add("body", DatatypeConverter.printHexBinary(body))
                .toString();
    }

}
