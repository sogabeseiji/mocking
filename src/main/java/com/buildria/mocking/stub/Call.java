package com.buildria.mocking.stub;

import com.google.common.base.MoreObjects;
import com.google.common.net.MediaType;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufHolder;
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

import static com.buildria.mocking.http.MockingHttpHeaders.CONTENT_TYPE;

public class Call {

    private String path;

    private String method;

    private final Map<String, String> headers = new ConcurrentHashMap<>();

    private final Map<String, List<String>> parameters = new ConcurrentHashMap<>();

    private byte[] body = new byte[]{};

    private MediaType contentType;

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
            if (CONTENT_TYPE.equalsIgnoreCase(entry.getKey())) {
                call.contentType = MediaType.parse(entry.getValue());
            }
        }
        if (req instanceof ByteBufHolder) {
            ByteBuf buf = ((ByteBufHolder) req).content();
            if (buf != null) {
                call.body = new byte[buf.readableBytes()];
                buf.readBytes(call.body);
            }
        }

        LOG.debug("### call: {}", call.toString());
        return call;
    }

    @Nonnull
    public String getPath() {
        return path;
    }

    @Nonnull
    public String getMethod() {
        return method;
    }

    @Nonnull
    public Map<String, String> getHeaders() {
        return headers;
    }

    @Nonnull
    public Map<String, List<String>> getParameters() {
        return parameters;
    }

    @Nullable
    public byte[] getBody() {
        return body;
    }

    @Nullable
    public MediaType getContentType() {
        return contentType;
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this).
                add("path", path).add("method", method).add("headers", headers).add("parameters", parameters).
                add("body", DatatypeConverter.printHexBinary(body)).
                toString();
    }

    private static final Logger LOG = LoggerFactory.getLogger(Call.class);
}
