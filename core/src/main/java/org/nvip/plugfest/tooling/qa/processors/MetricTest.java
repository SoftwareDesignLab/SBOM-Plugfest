package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.SBOM;

public interface MetricTest {
    Result test(SBOM sbom);
}
