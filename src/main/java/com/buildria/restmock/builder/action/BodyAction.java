package com.buildria.restmock.builder.action;

import com.buildria.restmock.RestMockException;
import com.buildria.restmock.serializer.ObjectSerializer;
import com.buildria.restmock.serializer.ObjectSerializerContext;
import com.buildria.restmock.serializer.ObjectSerializerFactory;
import com.google.common.base.MoreObjects;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

import static com.buildria.restmock.http.RMHttpHeaders.CONTENT_TYPE;

/**
 * BodyAction.
 */
public class BodyAction extends Action {

    private final Object content;

    private final List<Action> actions;

    public BodyAction(@Nonnull Matcher<?> path, @Nonnull Object content,
            @Nonnull List<Action> actions) {
        super(path);
        this.content = Objects.requireNonNull(content);
        this.actions = Objects.requireNonNull(actions);
    }

    @Nonnull
    @Override
    public HttpResponse apply(@Nonnull HttpRequest req, @Nonnull HttpResponse res) {
        Objects.requireNonNull(req);
        Objects.requireNonNull(res);
        HeaderAction contentType = getHeader(req.getUri(), CONTENT_TYPE, actions);
        if (contentType == null) {
            throw new RestMockException("No Content-Type found.");
        }
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(contentType.getValue());
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        try {
            return new RawBodyAction(getPath(),
                    os.serialize(content).getBytes(StandardCharsets.UTF_8)).apply(req, res);
        } catch (IOException ex) {
            throw new RestMockException("failed to serialize body.");
        }
    }

    @Override
    public MoreObjects.ToStringHelper objects() {
        return super.objects().add("content", content.toString());
    }

}
