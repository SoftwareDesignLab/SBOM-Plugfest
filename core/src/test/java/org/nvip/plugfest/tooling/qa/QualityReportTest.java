package org.nvip.plugfest.tooling.qa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.DependencyTree;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.SBOMType;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QualityReportTest {

    /**
     * Test Constants
     */

    /**
     * Test Variables
     */

    SBOM test_SBOM_a;

    /**
     * Setup
     */
    @BeforeEach
    public void setup() {
        test_SBOM_a = new SBOM(SBOMType.CYCLONE_DX, "1.2", "2", "supplier_two",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());
    }

    /**
     * Teardown
     */
    @AfterEach
    public void teardown() {
        test_SBOM_a = null;
    }

    /**
     * QualityReport Constructor Tests
     */

    @Test
    public void create_QualityReport_test() {
        QualityReport qualityReport = new QualityReport(test_SBOM_a.getSerialNumber());
        assertNotNull(qualityReport);
    }

    @Test
    public void create_QualityReport_no_serial_number_test() {
        QualityReport qualityReport = new QualityReport();
        assertNotNull(qualityReport);
    }

}
