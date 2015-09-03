package com.buildria.restmock.builder.action;

import com.google.common.base.MoreObjects;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

/**
 * StatusCodeAction.
 */
public class StatusCodeAction extends Action {
    
    private final int code;

    public StatusCodeAction(@Nonnull Matcher<?> path, int code) {
        super(path);
        this.code = code;
    }

    @Nonnull
    @Override
    public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
        Objects.requireNonNull(req);
        Objects.requireNonNull(res);
        res.setStatus(HttpResponseStatus.valueOf(code));
        return res;
    }

    @Override
    public MoreObjects.ToStringHelper objects() {
        return super.objects().add("code", code);
    }

}
