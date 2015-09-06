package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.RestMockException;
import com.buildria.mocking.stub.Call;
import com.google.common.net.MediaType;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.path.xml.XmlPath;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

public class BodyRule extends Rule {

    private final String path;

    private final Matcher<?> matcher;

    public BodyRule(String path, Matcher<?> matcher) {
        super();
        this.path = Objects.requireNonNull(path);
        this.matcher = Objects.requireNonNull(matcher);
    }

    @Override
    public boolean apply(@Nonnull Call call) {
        Objects.requireNonNull(call);

        byte[] body = call.getBody();
        if (body == null || body.length == 0) {
            return matcher.matches(body);
        }

        MediaType contentType = call.getContentType();
        if (contentType == null) {
            throw new RestMockException("No Content-Type header.");
        }
        Charset charset = contentType.charset().or(StandardCharsets.UTF_8);
        String content = new String(body, charset);
        Object obj = convertContent(contentType.subtype(), content, path);
        return matcher.matches(obj);
    }

    private Object convertContent(String subtype, String content, String path) {
        if ("xml".equalsIgnoreCase(subtype)) {
            return new XmlPath(content).get(path);
        } else if ("json".equalsIgnoreCase(subtype)) {
            return new JsonPath(content).get(path);
        }
        throw new RestMockException("Not supported Content-Type.");
    }

    @Override
    public String getDescription() {
        return String.format("[Body] path: (%s) matcher: (%s)", path, matcher.toString());
    }

}
