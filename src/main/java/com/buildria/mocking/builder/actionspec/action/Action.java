package com.buildria.mocking.builder.actionspec.action;

import com.google.common.base.MoreObjects;
import com.google.common.base.MoreObjects.ToStringHelper;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.hamcrest.Matcher;

// CHECKSTYLE:OFF
public abstract class Action {
// CHECKSTYLE:ON

    private final Matcher<?> path;

    public Action(@Nonnull Matcher<?> path) {
        this.path = Objects.requireNonNull(path);
    }

    @Nonnull
    public Matcher<?> getPath() {
        return path;
    }

    public boolean isApplicable(String path) {
        return this.path.matches(path);
    }

    @Nullable
    public HeaderAction getHeader(String path, String headerName, List<Action> actions) {
        for (Action action : actions) {
            if (action.isApplicable(path) && action instanceof HeaderAction) {
                HeaderAction ha = (HeaderAction) action;
                if (ha.getHeader().equalsIgnoreCase(headerName)) {
                    return ha;
                }
            }
        }
        return null;
    }

    @Nonnull
    public abstract HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res);

    public ToStringHelper objects() {
        return MoreObjects.toStringHelper(this).add("path", path);
    }

    @Override
    public String toString() {
        return objects().toString();
    }

}
