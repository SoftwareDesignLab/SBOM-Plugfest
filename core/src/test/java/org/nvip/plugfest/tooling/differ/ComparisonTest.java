package org.nvip.plugfest.tooling.differ;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.DependencyTree;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.SBOMType;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ComparisonTest {

    /**
     * Test Constants
     */

    public static final String TEST_SMALL_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/sbom.alpine.json";

    public static final String TEST_MEDIUM_CDX_JSON ="src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/trivy-0.39.0_celery-3.1.cdx.json";

    public static final String TEST_ANOTHER_SMALL_SYFT_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/cdx.json";

    /**
     * Setup Teardown Methods
     */
    @BeforeEach
    public void setup() {

    }

    @AfterEach
    public void teardown() {

    }


    /**
     * runComparison tests
     */
    @Test
    public void runComparison_should_create_list_of_reports_test() {

        SBOM test_SBOM_target = new SBOM(SBOMType.CYCLONE_DX, "1.4", "1", "supplier",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-01T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        SBOM test_SBOM_a = new SBOM(SBOMType.CYCLONE_DX, "1.2", "2", "supplier_two",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        SBOM test_SBOM_b = new SBOM(SBOMType.SPDX, "2", "2", "supplier",
                "b9fc484b-41c4-4589-b3ef-c57bba20078c", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        SBOM test_SBOM_c = new SBOM(SBOMType.CYCLONE_DX, "1.4", "3", "supplier_three",
                "urn:uuid:1b53623d-a11a-1111-1a11-f84b7f617c54", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        List<SBOM> test_SBOM_list = new ArrayList<>();
        test_SBOM_list.add(test_SBOM_a);
        test_SBOM_list.add(test_SBOM_b);
        test_SBOM_list.add(test_SBOM_c);

        Comparison test_comparison = new Comparison(test_SBOM_target);

        test_comparison.runComparison(test_SBOM_list);

        List<DiffReport> test_report_result = test_comparison.getDiffReports();

        assertNotNull(test_report_result);
        assertEquals(3, test_report_result.size());

    }


    /**
     * assignComponents tests
     */

    /**
     * getTargetSBOM tests
     */

    /**
     * getDiffReports
     */

    /**
     * getComparisons
     */

}
