package org.nvip.plugfest.tooling.qa.processors.tests;

import org.nvip.plugfest.tooling.qa.processors.MetricTest;
import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.SBOM;

public class EmptyOrNull implements MetricTest {
    private final String TEST_NAME = "EmptyOrNull";

    @Override
    public Result test(SBOM sbom) {
        return null;
    }
}
