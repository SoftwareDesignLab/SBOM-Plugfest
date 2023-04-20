package org.nvip.plugfest.tooling.metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SPDXMetricsTest {

    /**
     * Test Constants
     */
    private static final String TEST_SPDX_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/sample_boms";

    private static final String TEST_SPDX_SBOM_ONE = "sbom.docker.2-2.spdx";

    /**
     * Test Variables
     */

    /**
     * Setup
     */

    /**
     * Teardown
     */

    @Test
    public void create_SPDXMetrics_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
    }

}
