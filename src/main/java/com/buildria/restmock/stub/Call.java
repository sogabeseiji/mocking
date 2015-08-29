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
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.xml.bind.DatatypeConverter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Call {

    private String path;

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
        call.path = decoder.path();
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

        LOG.debug("### call: {}", call.toString());
        return call;
    }

    public @Nonnull String getPath() {
        return path;
    }

    public @Nonnull String getMethod() {
        return method;
    }

    public @Nonnull Map<String, String> getHeaders() {
        return headers;
    }

    public @Nonnull Map<String, List<String>> getParameters() {
        return parameters;
    }

    public @Nullable byte[] getBody() {
        return body;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("path", path).add("method", method).add("headers", headers).add("parameters", parameters).
                add("body", DatatypeConverter.printHexBinary(body))
                .toString();
    }

    private static final Logger LOG = LoggerFactory.getLogger(Call.class);
}
