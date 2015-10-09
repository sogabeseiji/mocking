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
package com.buildria.mocking.serializer;

import com.buildria.mocking.MockingException;
import com.buildria.mocking.TestNameRule;
import com.buildria.mocking.serializer.ObjectSerializerContext.SubType;
import java.nio.charset.StandardCharsets;
import org.junit.Rule;
import org.junit.Test;

import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertThat;

public class ObjectSerializerContextTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test
    public void testContentTypeWithCharset() {
        ObjectSerializerContext ctx
                 = new ObjectSerializerContext(SubType.XML, StandardCharsets.UTF_8);
        assertThat(ctx.getSubType(), is(SubType.XML));
        assertThat(ctx.getCharset().name(), is("UTF-8"));
    }

    @Test
    public void testCreateJAXB() {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.XML, StandardCharsets.UTF_8);
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(JAXBXmlSerializer.class));
    }

    @Test
    public void testCreateJackson() {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8);
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(JacksonJsonSerializer.class));
    }

    @Test
    public void testCreateGson() {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8) {
                    @Override
                    protected boolean isJacksonEnabled() {
                        return false;
                    }
                };
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
        assertThat(os, notNullValue());
        assertThat(os, instanceOf(GsonJsonSerializer.class));
    }

    @Test(expected = MockingException.class)
    public void testCreateNoJsonFound() throws Exception {
        ObjectSerializerContext ctx
                = new ObjectSerializerContext(SubType.JSON, StandardCharsets.UTF_8) {
                    @Override
                    protected boolean isGsonEnabled() {
                        return false;
                    }

                    @Override
                    protected boolean isJacksonEnabled() {
                        return false;
                    }
                };
        ObjectSerializer os = ObjectSerializerFactory.create(ctx);
    }
}
