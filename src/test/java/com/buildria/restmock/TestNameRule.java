package com.buildria.restmock;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author sogabe
 */
public class TestNameRule extends TestWatcher {

    private long start;

    @Override
    protected void starting(Description description) {
        start = System.currentTimeMillis();
        log.info("=== {} starting ...", description.getMethodName());
    }

    @Override
    protected void finished(Description description) {
        long end = System.currentTimeMillis();
        log.info("=== {} finished. {} ms", description.getMethodName(), end - start);
    }

    private final Logger log = LoggerFactory.getLogger(TestNameRule.class);
}
