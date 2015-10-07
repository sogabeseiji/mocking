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
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.buildria.mocking.builder.action;

import com.buildria.mocking.Mocking;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import org.apache.commons.lang3.text.StrSubstitutor;

// CHECKSTYLE:OFF
public abstract class ActionSpec {
// CHECKSTYLE:ON

    private String path;

    public ActionSpec(String path) {
        this.path = Objects.requireNonNull(path);
    }

    public String getPath() {
        return path;
    }

    public List<Action> getActions() {
        return Mocking.HOLDER.get().getActions(path);
    }

    public void addAction(Action action) {
        Mocking.HOLDER.get().addAction(path, action);
    }

    protected void resolvePath(String name, String value) {
        Map<String, String> map = new HashMap<>();
        map.put(name, value);
        this.path = StrSubstitutor.replace(path, map, "{", "}");
    }
}
