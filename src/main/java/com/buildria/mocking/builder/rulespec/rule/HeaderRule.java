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

import com.buildria.mocking.stub.Call;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;
import org.hamcrest.Matcher;

/**
 *
 * @author sogabe
 */
public class HeaderRule extends Rule {

    private final String name;

    private final Matcher<?> value;

    public HeaderRule(@Nonnull String name, @Nonnull Matcher<?> value) {
        super();
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
    }

    @Override
    public boolean apply(@Nonnull Call call) {
        Objects.requireNonNull(call);
        Map<String, String> headers = call.getHeaders();
        for (Map.Entry<String, String> entry : headers.entrySet()) {
            String n = entry.getKey();
            String v = entry.getValue();
            if (name.equalsIgnoreCase(n) && value.matches(v)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String getDescription() {
        return String.format("[Header] name: (%s) value: (%s)", name, value.toString());
    }

}
