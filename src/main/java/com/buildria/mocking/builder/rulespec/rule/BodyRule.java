/**
 * The MIT License (MIT)
 *
 * Copyright (c) 2015 Seiji Sogabe
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.buildria.mocking.builder.rulespec.rule;

import com.buildria.mocking.MockingException;
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
            throw new MockingException("No Content-Type header.");
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
        throw new MockingException("Not supported Content-Type.");
    }

    @Override
    public String getDescription() {
        return String.format("[Body] path: (%s) matcher: (%s)", path, matcher.toString());
    }

}
