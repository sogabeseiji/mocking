package com.buildria.mocking.serializer;

import com.buildria.mocking.TestNameRule;
import org.junit.Rule;
import org.junit.Test;

/**
 * Test for ObjectSerializerFactory.
 *
 * @author Seiji Sogabe
 */
public class ObjectSerializerFactoryTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    @Test(expected = NullPointerException.class)
    public void testCreate() {
        ObjectSerializerFactory.create(null);
    }

}
