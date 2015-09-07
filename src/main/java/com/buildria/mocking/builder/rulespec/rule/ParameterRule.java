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
import com.google.common.base.Joiner;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Nonnull;

public class ParameterRule extends Rule {

    private final String key;

    private final String[] values;

    public ParameterRule(String key, @Nonnull String[] values) {
        super();
        this.key = Objects.requireNonNull(key);
        this.values = Objects.requireNonNull(values);
        Arrays.sort(this.values);
    }

    @Override
    public boolean apply(@Nonnull Call call) {
        Objects.requireNonNull(call);
        Map<String, List<String>> params = call.getParameters();
        List<String> vals = params.get(key);
        if (vals == null) {
            return false;
        }
        String[] sorted = vals.toArray(new String[0]);
        Arrays.sort(sorted);
        return Arrays.equals(sorted, values);
    }

    @Override
    public String getDescription() {
        return String.format("[Parameter] key: (%s) value: (%s)", key, Joiner.on(",").join(values));
    }

}
