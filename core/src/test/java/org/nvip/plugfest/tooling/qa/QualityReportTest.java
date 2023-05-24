package org.nvip.plugfest.tooling.qa;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.qa.test_results.TestResults;
import org.nvip.plugfest.tooling.sbom.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * File: QualityReportTest.java
 * Tests for QualityReport
 *
 * @author Tyler Drake
 */
public class QualityReportTest {

    /**
     * Test Constants
     */

    private static final String TEST_MESSAGE = "This is a test.";

    private static final String TEST_MESSAGE_TWO = "This is also a test.";

    private static final String TEST_MESSAGE_THREE = "This may as well be test.";

    private static final String TEST_MESSAGE_FOUR = "This is indeed again, a test.";

    private static final int TEST_PASSED_COMPONENTS_FAILING_TEST = 0;

    private static final int TEST_PASSED_COMPONENTS_NO_TESTS = 0;

    private static final int TEST_PASSED_COMPONENTS_ONE = 1;

    private static final int TEST_PASSED_COMPONENTS_TWO = 2;

    private static final String TO_STRING_RESULT =
            "QualityReport{serialNumber=INVALID_SN,\n" +
            "\n" +
            "TEST RESULTS - TOTAL COMPONENTS PASSED: 1/1\n" +
            "\n" +
            "Component 'red' PASSED with 2/2 Tests Passed:\n" +
            "  PASSED: This is a test.\n" +
            "  PASSED: This is also a test.\n" +
            "]}";

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
    public void setup() throws Exception {
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

//    @Test
//    public void create_QualityReport_no_serial_number_test() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport);
//    }

    /**
     * addTestResult Tests
     */

//    @Test
//    public void addTestResult_test() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport);
//        qualityReport.addTestResult(test_results_one);
//    }

    /**
     * append Tests
     */

//    @Test
//    public void append_test() {
//
//        QualityReport qualityReport_one = new QualityReport(test_SBOM_a.getSerialNumber());
//        assertNotNull(qualityReport_one);
//
//        qualityReport_one.addTestResult(test_results_one);
//
//        QualityReport qualityReport_two = new QualityReport();
//        assertNotNull(qualityReport_two);
//
//        qualityReport_two.addTestResult(test_results_two);
//
//        qualityReport_one.append(qualityReport_two);
//
//    }

    /**
     * getPassedComponents Tests
     */

//    @Test
//    public void getPassedComponents_test() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport);
//        assertNotNull(qualityReport.getPassedComponents());
//    }

//    @Test
//    public void getPassedComponents_has_correct_passed_count_test() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport);
//
//        test_results_one.addTest(test_test_one); // passing test
//        test_results_one.addTest(test_test_two); // passing test
//        qualityReport.addTestResult(test_results_one);
//
//        int passed = qualityReport.getPassedComponents();
//        assertEquals(TEST_PASSED_COMPONENTS_ONE, passed);
//    }

//    @Test
//    public void getPassedComponents_does_not_count_components_with_failing_tests_test() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport);
//
//        test_results_one.addTest(test_test_one); // passing test
//        test_results_one.addTest(test_test_three); // failing test
//        qualityReport.addTestResult(test_results_one);
//
//        int passed = qualityReport.getPassedComponents();
//        assertEquals(TEST_PASSED_COMPONENTS_FAILING_TEST, passed);
//    }
//
//    @Test
//    public void getPassedComponents_has_correct_passed_count_multiple_components_test() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport);
//
//        test_results_one.addTest(test_test_one); // passing test
//        qualityReport.addTestResult(test_results_one);
//
//        test_results_two.addTest(test_test_two); // passing test
//        qualityReport.addTestResult(test_results_two);
//
//        int passed = qualityReport.getPassedComponents();
//        assertEquals(TEST_PASSED_COMPONENTS_TWO, passed);
//    }
//
//    @Test
//    public void getPassedComponents_returns_zero_if_no_tests() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport);
//        int passed = qualityReport.getPassedComponents();
//        assertEquals(TEST_PASSED_COMPONENTS_NO_TESTS, passed);
//    }
//
//    /**
//     * toString Tests
//     */
//
//    @Test
//    public void toString_test() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport.toString());
//    }
//
//    @Test
//    public void toString_contains_correct_information_test() {
//        QualityReport qualityReport = new QualityReport();
//        assertNotNull(qualityReport.toString());
//
//        test_results_one.addTest(test_test_one); // passing test
//        test_results_one.addTest(test_test_two); // passing test
//        qualityReport.addTestResult(test_results_one);
//
//        String toString_result = qualityReport.toString();
//        assertEquals(TO_STRING_RESULT, toString_result);
//    }

}
