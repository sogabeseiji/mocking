package com.buildria.restmock.builder.actionspec.action;

import com.buildria.restmock.http.RMHttpHeaders;
import com.google.common.base.MoreObjects;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.xml.bind.DatatypeConverter;
import org.hamcrest.Matcher;

/**
 * RawBodyAction.
 */
public class RawBodyAction extends Action {

    private final byte[] content;

    public RawBodyAction(@Nonnull Matcher<?> path, @Nonnull byte[] content) {
        super(path);
        this.content = Objects.requireNonNull(content);
    }

    @Nonnull
    @Override
    public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
        Objects.requireNonNull(req);
        Objects.requireNonNull(res);
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(content.length);
        buffer.writeBytes(content);
        HttpResponse r = new DefaultFullHttpResponse(res.getProtocolVersion(), res.getStatus(), buffer);
        for (Map.Entry<String, String> entry : res.headers()) {
            r.headers().add(entry.getKey(), entry.getValue());
        }
        r.headers().add(RMHttpHeaders.CONTENT_LENGTH, content.length);
        return r;
    }

    @Override
    public MoreObjects.ToStringHelper objects() {
        return super.objects().add("content", DatatypeConverter.printHexBinary(content));
    }

}
