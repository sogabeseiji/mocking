package com.buildria.mocking.builder.actionspec.action;

import com.google.common.base.MoreObjects;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

/**
 * HeaderAction.
 */
public class HeaderAction extends Action {

    private final String header;

    private final String value;

    @Nonnull
    public HeaderAction(@Nonnull Matcher<?> path, @Nonnull String header,
            @Nonnull String value) {
        super(path);
        this.header = Objects.requireNonNull(header);
        this.value = Objects.requireNonNull(value);
    }

    @Nonnull
    public String getHeader() {
        return header;
    }

    @Nonnull
    public String getValue() {
        return value;
    }

    @Nonnull
    @Override
    public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
        Objects.requireNonNull(req);
        Objects.requireNonNull(res);
        res.headers().add(header, value);
        return res;
    }

    @Override
    public MoreObjects.ToStringHelper objects() {
        return super.objects().add("header", header).add("value", value);
    }

}
