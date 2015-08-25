package com.buildria.restmock;

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
        log.info("=== {} starting ...", description.getMethodName());
    }

    @Override
    protected void finished(Description description) {
        sw.stop();
        log.info("=== {} finished. {}", description.getMethodName(), sw);
    }

    private final Logger log = LoggerFactory.getLogger(TestNameRule.class);
}
