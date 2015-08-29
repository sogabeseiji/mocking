package com.buildria.restmock.stub;

import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.HttpContent;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import javax.xml.bind.DatatypeConverter;

public class Call {

    private String uri;

    private String method;

    private final Map<String, String> headers = new ConcurrentHashMap<>();

    private final Map<String, List<String>> parameters = new ConcurrentHashMap<>();

    private byte[] body;

    private Call() {
        //
    }

    public static Call fromRequest(HttpRequest req) {
        Objects.requireNonNull(req);
        Call call = new Call();
        QueryStringDecoder decoder = new QueryStringDecoder(req.getUri());
        call.uri = decoder.uri();
        call.parameters.putAll(decoder.parameters());
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

    public Map<String, List<String>> getParameters() {
        return parameters;
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
