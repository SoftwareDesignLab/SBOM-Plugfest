package org.nvip.plugfest.tooling.qa.processors.tests;

import org.nvip.plugfest.tooling.qa.processors.MetricTest;
import org.nvip.plugfest.tooling.qa.test_results.Result;

public class EmptyOrNullTest implements MetricTest {
    private final String TEST_NAME = "EmptyOrNull";


    @Override
    public Result test(Object o) {
        return null;
    }
}
