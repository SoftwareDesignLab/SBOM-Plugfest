package org.nvip.plugfest.tooling.metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CDXMetricsTest {

    /**
     * Test Constants
     */

    private static final String TEST_CDX_JSON_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/";

    private static final String TEST_CDX_SBOM_ONE = "trivy-0.39.0_celery-3.1.cdx.json";

    private static final String TEST_CDX_SBOM_TWO = "sbom.alpine.json";

    private static final String TEST_CDX_SBOM_THREE = "cdx.json";

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
    public void create_CDXMetrics_test() {

        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_CDX_JSON_SBOM_PATH, TEST_CDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);

    }

}
