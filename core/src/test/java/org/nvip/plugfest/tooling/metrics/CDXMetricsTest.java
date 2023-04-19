package org.nvip.plugfest.tooling.metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CDXMetricsTest {

    /**
     * Test Constants
     */

    private static final String TEST_CDX_JSON_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/";

    private static final String TEST_FAKE_CDX_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/fake_sbom_path/";

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

    @Test
    public void CDXMetrics_getFilepath_test() {

        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_CDX_JSON_SBOM_PATH, TEST_CDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        String file_path_result = test_cdx_metric.getFilepath();
        assertEquals(TEST_CDX_JSON_SBOM_PATH, file_path_result);

    }

    @Test
    public void CDXMetrics_setFilepath_test() {

        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_CDX_JSON_SBOM_PATH, TEST_CDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        test_cdx_metric.setFilepath(TEST_FAKE_CDX_SBOM_PATH);
        assertEquals(TEST_FAKE_CDX_SBOM_PATH, test_cdx_metric.getFilepath());

    }

}
