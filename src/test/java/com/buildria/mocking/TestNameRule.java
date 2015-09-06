package com.buildria.mocking;

import com.google.common.base.Stopwatch;
import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.common.base.Stopwatch.createStarted;

/**
 *
 * @author sogabe
 */
public class TestNameRule extends TestWatcher {

    private Stopwatch sw;

    @Override
    protected void starting(Description description) {
        sw = createStarted();
        log.debug("=== {} starting ...", description.getMethodName());
    }

    @Override
    protected void finished(Description description) {
        sw.stop();
        log.debug("=== {} finished. {}", description.getMethodName(), sw);
    }

    private final Logger log = LoggerFactory.getLogger(TestNameRule.class);
}