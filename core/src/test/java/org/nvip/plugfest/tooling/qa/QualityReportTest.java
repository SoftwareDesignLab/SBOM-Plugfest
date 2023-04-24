package org.nvip.plugfest.tooling.qa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.qa.test_results.TestResults;
import org.nvip.plugfest.tooling.sbom.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class QualityReportTest {

    /**
     * Test Constants
     */

    // QualityReport Constants

    // Test Class Constants
    private static final String TEST_MESSAGE = "This is a test.";

    private static final String TEST_MESSAGE_TWO = "This is also a test.";

    private static final String TEST_MESSAGE_THREE = "This may as well be test.";

    private static final String TEST_MESSAGE_FOUR = "This is indeed again, a test.";

    // TestResults Constants


    /**
     * Test Variables
     */

    SBOM test_SBOM_a;

    Component test_component_a;

    Component test_component_b;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_one;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_two;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_three;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_four;

    TestResults test_results_one;

    TestResults test_results_two;

    /**
     * Setup
     */
    @BeforeEach
    public void setup() {
        // Create SBOMs
        test_SBOM_a = new SBOM(SBOMType.CYCLONE_DX, "1.2", "2", "supplier_two",
                "urn:uuid:1b53623d-b96b-4660-8d25-f84b7f617c54", "2023-01-02T02:36:00-05:00",
                new HashSet<>(), new DependencyTree());

        // Create Components
        test_component_a = new Component(
                "red", "red_publisher", "1.1.0",
                Set.of("cpe2.3::test_red_cpe"), Set.of(new PURL("pkg:redpackage/red@1.1.0")), Set.of("random_red_swid")
        );

        test_component_b = new Component(
                "blue", "blue_publisher", "1.1.0",
                Set.of("cpe2.3::test_blue_cpe"), Set.of(new PURL("pkg:bluepackage/blue@1.1.0")), Set.of("random_blue_swid")
        );

        // Create some Tests
        test_test_one = new org.nvip.plugfest.tooling.qa.test_results.Test(true, TEST_MESSAGE);

        test_test_two = new org.nvip.plugfest.tooling.qa.test_results.Test(true, TEST_MESSAGE_TWO);

        test_test_three = new org.nvip.plugfest.tooling.qa.test_results.Test(false, TEST_MESSAGE_THREE);

        test_test_four = new org.nvip.plugfest.tooling.qa.test_results.Test(true, TEST_MESSAGE_FOUR);

        // Create TestResults
        test_results_one = new TestResults(test_component_a);

        test_results_two = new TestResults(test_component_b);


    }

    /**
     * Teardown
     */

    @AfterEach
    public void teardown() {
        test_SBOM_a = null;
        test_component_a = null;
        test_component_b = null;
        test_test_one = null;
        test_test_two = null;
        test_test_three = null;
        test_test_four = null;
        test_results_one = null;
        test_results_two = null;
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

    /**
     * addTestResult Tests
     */

    @Test
    public void addTestResult_test() {
        QualityReport qualityReport = new QualityReport();
        assertNotNull(qualityReport);
        qualityReport.addTestResult(test_results_one);
    }

    /**
     * append Tests
     */

    @Test
    public void append_test() {

        QualityReport qualityReport_one = new QualityReport(test_SBOM_a.getSerialNumber());
        assertNotNull(qualityReport_one);

        qualityReport_one.addTestResult(test_results_one);

        QualityReport qualityReport_two = new QualityReport();
        assertNotNull(qualityReport_two);

        qualityReport_two.addTestResult(test_results_two);

        qualityReport_one.append(qualityReport_two);

    }

    @Test
    public void getPassedComponents_test() {
        QualityReport qualityReport = new QualityReport();
        assertNotNull(qualityReport);
        int passed = qualityReport.getPassedComponents();
        assertNotNull(passed);
    }

}
