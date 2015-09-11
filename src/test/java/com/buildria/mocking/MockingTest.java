package com.buildria.mocking;

import org.junit.Rule;
import org.junit.Test;

public class MockingTest {

    @Rule
    public TestNameRule testNameRule = new TestNameRule();

    private Mocking target;

    @Test(expected = IllegalArgumentException.class)
    public void  testConstructorNegativePort() throws Exception {
        target = new Mocking(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void  testConstructorOverPort() throws Exception {
        target = new Mocking(65536);
    }
    
}
