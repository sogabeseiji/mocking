package com.buildria.mocking.builder.actionspec.action;

import com.google.common.base.MoreObjects;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import org.hamcrest.Matcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DelayAction extends Action {

    private final long wait;

    public DelayAction(Matcher<?> path, long wait) {
        super(path);
        if (wait < 0) {
            throw new IllegalArgumentException("wait should be non negative");
        }
        this.wait = wait;
    }

    @Override
    public HttpResponse apply(HttpRequest req, HttpResponse res) {
        try {
            Thread.sleep(wait);
        } catch (InterruptedException e) {
            LOG.warn("failed to delay {}ms", wait);
        }

        return res;
    }

    @Override
    public MoreObjects.ToStringHelper objects() {
        return super.objects().add("wait", wait);
    }

    private static final Logger LOG = LoggerFactory.getLogger(DelayAction.class);
}
