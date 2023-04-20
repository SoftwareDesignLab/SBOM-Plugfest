package org.nvip.plugfest.tooling.metrics;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class SPDXMetricsTest {

    /**
     * Test Constants
     */
    private static final String TEST_SPDX_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/sample_boms";

    private static final String TEST_FAKE_SPDX_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/fake_boms";

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

    @Test
    public void SPDXMetrics_getFilepath_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        String file_path_result = test_spdx_metric.getFilepath();
        assertEquals(TEST_SPDX_SBOM_PATH, file_path_result);
    }

    @Test
    public void SPDXMetrics_setFilepath_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        test_spdx_metric.setFilepath(TEST_FAKE_SPDX_SBOM_PATH);
        String file_path_result = test_spdx_metric.getFilepath();
        assertEquals(TEST_FAKE_SPDX_SBOM_PATH, file_path_result);
    }

    @Test
    public void SPDXMetrics_verifySPDX_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        ArrayList<String> verify_results = test_spdx_metric.verifySPDX();
        assertNotNull(verify_results);
    }

}
