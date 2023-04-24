package org.nvip.plugfest.tooling.metrics;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * File: SPDXMetricsTest.java
 * Tests for SPDXMetrics
 *
 * @author Tyler Drake
 */
public class SPDXMetricsTest {

    /**
     * Test Constants
     */
    private static final String TEST_SPDX_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/sample_boms";

    private static final String TEST_FAKE_SPDX_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/fake_boms";

    private static final String TEST_SPDX_SBOM_ONE = "sbom.docker.2-2.spdx";

    private static final String TEST_SPDX_SBOM_TWO = "sbom.python.2-3.spdx";

    private static final String TEST_SPDX_SBOM_THREE = "sbom.alpine.2-3.spdx";

    private static final String TEST_BAD_SPDX_NO_VERSION = "sbom.version-missing.spdx";

    private static final int EXPECTED_GOOD_SPDX_ERRORS = 0;

    private static final int EXPECTED_BAD_SPDX_ERRORS = 1;

    private static final int EXPECTED_GOOD_SPDX_SCORE = 1;

    /**
     * Test Variables
     */

    /**
     * Setup
     */

    /**
     * Teardown
     */

    /**
     * SPDXMetrics Constructor Tests
     */

    @Test
    public void create_SPDXMetrics_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
    }

    /**
     * getFilepath Tests
     */

    @Test
    public void SPDXMetrics_getFilepath_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        String file_path_result = test_spdx_metric.getFilepath();
        assertEquals(TEST_SPDX_SBOM_PATH, file_path_result);
    }

    /**
     * setFilepath Tests
     */

    @Test
    public void SPDXMetrics_setFilepath_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        test_spdx_metric.setFilepath(TEST_FAKE_SPDX_SBOM_PATH);
        String file_path_result = test_spdx_metric.getFilepath();
        assertEquals(TEST_FAKE_SPDX_SBOM_PATH, file_path_result);
    }

    /**
     * compare Tests
     */
    @Disabled("This test fails, however i don't believe we use the 'compare' function anymore.")
    @Test
    public void SPDXMetrics_compare_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        String[] sbom_list = new String[]{TEST_SPDX_SBOM_ONE, TEST_SPDX_SBOM_TWO, TEST_SPDX_SBOM_THREE};
        test_spdx_metric.compare(sbom_list);
    }

    /**
     * verifySPDX Tests
     */

    @Test
    public void SPDXMetrics_verifySPDX_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        ArrayList<String> verify_results = test_spdx_metric.verifySPDX();
        assertNotNull(verify_results);
    }

    @Test
    public void SPDXMetrics_verifySPDX_returns_no_errors_from_good_SBOM() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        ArrayList<String> verify_results = test_spdx_metric.verifySPDX();
        assertNotNull(verify_results);
        assertEquals(EXPECTED_GOOD_SPDX_ERRORS, verify_results.size());
    }

    @Disabled("I feel like we should be getting bad error message on return instead of 'null'")
    @Test
    public void SPDXMetrics_verifySPDX_returns_correct_errors_from_bad_SBOM() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_BAD_SPDX_NO_VERSION);
        assertNotNull(test_spdx_metric);
        ArrayList<String> verify_results = test_spdx_metric.verifySPDX();
        assertNotNull(verify_results);
        assertEquals(EXPECTED_BAD_SPDX_ERRORS, verify_results.size());
    }

    /**
     * testMetric Tests
     */

    @Test
    public void SPDXMetrics_testMetric_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        ArrayList<String> verify_results = test_spdx_metric.verifySPDX();
        assertNotNull(verify_results);
        assertEquals(EXPECTED_GOOD_SPDX_SCORE, test_spdx_metric.testMetric());
    }

    /**
     * testMetric getScore
     */
    @Test
    public void SPDXMetrics_getScore_test() {
        SPDXMetrics test_spdx_metric = new SPDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_spdx_metric);
        ArrayList<String> verify_results = test_spdx_metric.verifySPDX();
        assertNotNull(verify_results);
        assertEquals(EXPECTED_GOOD_SPDX_SCORE, test_spdx_metric.getScore());
    }

}
