package org.nvip.plugfest.tooling.metrics;

import org.cyclonedx.CycloneDxSchema;
import org.cyclonedx.model.Hash;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CDXMetricsTest {

    /**
     * Test Constants
     */

    private static final String TEST_CDX_JSON_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/";

    private static final String TEST_FAKE_CDX_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/fake_sbom_path/";

    private static final String TEST_CDX_SBOM_ONE = "trivy-0.39.0_celery-3.1.cdx.json";

    private static final String TEST_CDX_SBOM_TWO = "sbom.alpine.json";

    private static final String TEST_CDX_SBOM_THREE = "cdx.json";

    private static final String TEST_SPDX_SBOM_PATH = "src/test/java/org/nvip/plugfest/tooling/sample_boms/";

    private static final String TEST_SPDX_SBOM_ONE = "sbom.docker.2-2.spdx";

    private static final int EXPECTED_SBOM_ONE_V10_HASHES = 6;

    private static final int EXPECTED_SBOM_ONE_V11_HASHES = 6;

    private static final int EXPECTED_SBOM_ONE_V12_HASHES = 8;

    private static final int EXPECTED_SBOM_ONE_V13_HASHES = 8;

    private static final int EXPECTED_SBOM_ONE_V14_HASHES = 8;



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

    @Test
    public void CDXMetrics_verifyCDX_version_1_4_cdx_test() {

        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_CDX_JSON_SBOM_PATH, TEST_CDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, Boolean> verify_result = test_cdx_metric.verifyCDX(TEST_CDX_SBOM_ONE);
        assertNotNull(verify_result);

        for(Boolean result : verify_result.values()) {
            assertEquals(true, result);
        }

    }

    @Disabled("Possible Bug; Why are we getting 'true' for all CDX Schema from an SBOM that doesn't exist?")
    @Test
    public void CDXMetrics_verifyCDX_returns_null_on_invalid_SBOM_test() {
        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_CDX_JSON_SBOM_PATH, "");
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, Boolean> verify_result = test_cdx_metric.verifyCDX("");
        assertNull(verify_result);
    }

    @Disabled(
            "Possible Bug; same problem as above but with 'null' now. Shouldn't we be getting no matches?" +
            "I would assume the verify check would tell a 'null' object it doesn't match any CDX schemas."
    )
    @Test
    public void CDXMetrics_verifyCDX_returns_null_on_null_SBOM_test() {
        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_CDX_JSON_SBOM_PATH, null);
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, Boolean> verify_result = test_cdx_metric.verifyCDX(null);
        assertNull(verify_result);
    }

    @Disabled("Possible Bug; same issue as above but this time with the path being empty")
    @Test
    public void CDXMetrics_verifyCDX_returns_null_on_no_path_test() {
        CDXMetrics test_cdx_metric = new CDXMetrics("", TEST_CDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, Boolean> verify_result = test_cdx_metric.verifyCDX(TEST_CDX_SBOM_ONE);
        assertNull(verify_result);
    }

    @Disabled("Possible Bug; same issue as above but this time with the path being null")
    @Test
    public void CDXMetrics_verifyCDX_returns_null_on_null_path_test() {
        CDXMetrics test_cdx_metric = new CDXMetrics(null, TEST_CDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, Boolean> verify_result = test_cdx_metric.verifyCDX(TEST_CDX_SBOM_ONE);
        assertNull(verify_result);
    }

    @Disabled("Possible Bug; An SPDX SBOM is getting true for every CDX schema")
    @Test
    public void CDXMetrics_verifyCDX_fails_when_given_SPDX_SBOM_test() {
        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, Boolean> verify_result = test_cdx_metric.verifyCDX(TEST_CDX_SBOM_ONE);
        assertNull(verify_result);
    }

    @Test
    public void CDXMetrics_calculateHashes_test() {
        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, List<Hash>> calculate_result = test_cdx_metric.calculateHashes(TEST_SPDX_SBOM_ONE);
        assertNotNull(calculate_result);
    }

    @Test
    public void CDXMetrics_calculateHashes_should_have_correct_amount_of_hashes_test() {
        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_SPDX_SBOM_PATH, TEST_SPDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, List<Hash>> calculate_result = test_cdx_metric.calculateHashes(TEST_SPDX_SBOM_ONE);
        assertNotNull(calculate_result);

        assertEquals(EXPECTED_SBOM_ONE_V10_HASHES, calculate_result.get(CycloneDxSchema.Version.VERSION_10).size());

        assertEquals(EXPECTED_SBOM_ONE_V11_HASHES, calculate_result.get(CycloneDxSchema.Version.VERSION_11).size());

        assertEquals(EXPECTED_SBOM_ONE_V12_HASHES, calculate_result.get(CycloneDxSchema.Version.VERSION_12).size());

        assertEquals(EXPECTED_SBOM_ONE_V13_HASHES, calculate_result.get(CycloneDxSchema.Version.VERSION_13).size());

        assertEquals(EXPECTED_SBOM_ONE_V14_HASHES, calculate_result.get(CycloneDxSchema.Version.VERSION_14).size());

    }

    @Disabled("Bad Test; Putting this on the back burner for now.")
    @Test
    public void CDXMetrics_calculateHashes_should_have_real_hashes_test() {
        CDXMetrics test_cdx_metric = new CDXMetrics(TEST_CDX_JSON_SBOM_PATH, TEST_CDX_SBOM_ONE);
        assertNotNull(test_cdx_metric);
        HashMap<CycloneDxSchema.Version, List<Hash>> calculate_result = test_cdx_metric.calculateHashes(TEST_SPDX_SBOM_ONE);
        assertNotNull(calculate_result);

        List<Hash> test_hash_v10 = calculate_result.get(CycloneDxSchema.Version.VERSION_10);
        List<Hash> test_hash_v11 = calculate_result.get(CycloneDxSchema.Version.VERSION_11);
        List<Hash> test_hash_v12 = calculate_result.get(CycloneDxSchema.Version.VERSION_12);
        List<Hash> test_hash_v13 = calculate_result.get(CycloneDxSchema.Version.VERSION_13);
        List<Hash> test_hash_v14 = calculate_result.get(CycloneDxSchema.Version.VERSION_14);

        for (Hash hash : test_hash_v10) { assertInstanceOf(Hash.class, hash); }
    }
}
