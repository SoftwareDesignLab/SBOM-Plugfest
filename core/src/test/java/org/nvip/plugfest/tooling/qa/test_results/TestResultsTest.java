package org.nvip.plugfest.tooling.qa.test_results;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.nvip.plugfest.tooling.sbom.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class TestResultsTest {

    /**
     * Test Constants
     */

    private static final String TEST_MESSAGE = "This is a test.";

    private static final String TEST_MESSAGE_TWO = "This is also a test.";

    private static final String TEST_MESSAGE_THREE = "This may as well be test.";

    private static final String TEST_MESSAGE_FOUR = "This is indeed again, a test.";


    private static final String TO_STRING_PASS_MESSAGE = "PASSED: " + TEST_MESSAGE;

    private static final String TO_STRING_FAIL_MESSAGE = "FAILED: " + TEST_MESSAGE;

    private static final int EXPECTED_ADDTESTS_RESULT = 3;

    /**
     * Test Variables
     */

    Component test_component_a;

    Component test_component_b;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_a;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_b;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_c;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_d;


    /**
     * Setup
     */
    @BeforeEach
    public void setup() {

        // Initialize component
        test_component_a = new Component(
                "red", "red_publisher", "1.1.0",
                Set.of("cpe2.3::test_red_cpe"), Set.of(new PURL("pkg:redpackage/red@1.1.0")), Set.of("random_red_swid")
        );

        test_component_b = new Component(
                "blue", "blue_publisher", "1.1.0",
                Set.of("cpe2.3::test_blue_cpe"), Set.of(new PURL("pkg:bluepackage/blue@1.1.0")), Set.of("random_blue_swid")
        );

        test_test_a = new org.nvip.plugfest.tooling.qa.test_results.Test(true, TEST_MESSAGE);

        test_test_b = new org.nvip.plugfest.tooling.qa.test_results.Test(true, TEST_MESSAGE_TWO);

        test_test_c = new org.nvip.plugfest.tooling.qa.test_results.Test(true, TEST_MESSAGE_THREE);

        test_test_d = new org.nvip.plugfest.tooling.qa.test_results.Test(true, TEST_MESSAGE_FOUR);


    }

    /**
     * Teardown
     */
    @AfterEach
    public void teardown() {
        test_component_a = null;
        test_component_b = null;
    }

    /**
     * TestResults Constructor Tests
     */

    @Test
    public void create_TestResults_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
    }

    /**
     * getComponent Tests
     */

    @Test
    public void TestResults_getComponent_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
        Component component_result = test_tr.getComponent();
        assertEquals(test_component_a, component_result);
    }

    @Test
    public void TestResults_getComponent_on_null_component() {
        TestResults test_tr = new TestResults(null);
        assertNotNull(test_tr);
        Component component_result = test_tr.getComponent();
        assertEquals(null, component_result);
    }

    /**
     * addTest Tests
     */

    @Test
    public void TestResults_addTest_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
        test_tr.addTest(test_test_a);
    }

    @Test
    public void TestResults_addTest_null_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
        test_tr.addTest(null);
    }

    @Test
    public void TestResult_addTest_to_a_null_component_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
        test_tr.addTest(test_test_a);
    }

    @Test
    public void TestResult_addTest_should_not_work_on_null_TestResults_test() {
        TestResults test_tr = null;
        assertNull(test_tr);
        assertThrows(NullPointerException.class, () -> test_tr.addTest(test_test_a));
    }

    /**
     * getTests Tests
     */
    @Test
    public void TestResult_getTest_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
        test_tr.addTest(test_test_a);
        List<org.nvip.plugfest.tooling.qa.test_results.Test> getTestResults = test_tr.getTests();
        assertNotNull(getTestResults);
    }

    @Test
    public void TestResult_getTest_should_have_correct_results_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
        test_tr.addTest(test_test_a);
        List<org.nvip.plugfest.tooling.qa.test_results.Test> getTestResults = test_tr.getTests();
        assertNotNull(getTestResults);
        assertEquals(test_test_a, getTestResults.get(0));
    }

    @Test
    public void TestResult_getTest_should_be_empty_for_TestResult_with_no_tests_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
        List<org.nvip.plugfest.tooling.qa.test_results.Test> getTestResults = test_tr.getTests();
        assertTrue(getTestResults.isEmpty());
    }

    @Disabled("Possible Bug; do we want test results exists for a TestResult object without a component?")
    @Test
    public void TestResult_getTest_should_return_nothing_for_TestResult_with_no_component_test() {
        TestResults test_tr = new TestResults(null);
        assertNotNull(test_tr);
        test_tr.addTest(test_test_a);
        List<org.nvip.plugfest.tooling.qa.test_results.Test> getTestResults = test_tr.getTests();
        assertNotNull(getTestResults);
        assertEquals(test_test_a, getTestResults.get(0));
    }

    /**
     * addTests Tests
     */

    @Test
    public void TestResult_addTests_test() {
        TestResults test_tr_one = new TestResults(test_component_a);
        assertNotNull(test_tr_one);
        test_tr_one.addTest(test_test_a);
        test_tr_one.addTest(test_test_b);
        test_tr_one.addTest(test_test_c);

        TestResults test_tr_two = new TestResults(test_component_b);
        assertNotNull(test_tr_two);
        test_tr_two.addTests(test_tr_one);
    }

    @Test
    public void TestResult_addTests_has_correct_tests_test() {
        TestResults test_tr_one = new TestResults(test_component_a);
        assertNotNull(test_tr_one);
        test_tr_one.addTest(test_test_a);
        test_tr_one.addTest(test_test_b);
        test_tr_one.addTest(test_test_c);

        TestResults test_tr_two = new TestResults(test_component_b);
        assertNotNull(test_tr_two);
        test_tr_two.addTests(test_tr_one);

        assertEquals(EXPECTED_ADDTESTS_RESULT, test_tr_two.getTests().size());

        assertEquals(test_tr_one.getTests(), test_tr_two.getTests());
    }

    @Test
    public void TestResult_addTests_should_merge_tests_test() {
        TestResults test_tr_one = new TestResults(test_component_a);
        assertNotNull(test_tr_one);
        test_tr_one.addTest(test_test_a);
        test_tr_one.addTest(test_test_b);

        TestResults test_tr_two = new TestResults(test_component_b);
        assertNotNull(test_tr_two);
        test_tr_two.addTest(test_test_c);
        test_tr_two.addTest(test_test_d);

        test_tr_two.addTests(test_tr_one);

        ArrayList<org.nvip.plugfest.tooling.qa.test_results.Test> expected = new ArrayList<>();
        expected.add(test_test_a);
        expected.add(test_test_b);
        expected.add(test_test_c);
        expected.add(test_test_d);
        ArrayList<org.nvip.plugfest.tooling.qa.test_results.Test> result = test_tr_two.getTests();

        for(org.nvip.plugfest.tooling.qa.test_results.Test current_test : result) {
            assertTrue(expected.contains(current_test));
        }
        for(org.nvip.plugfest.tooling.qa.test_results.Test current_test : expected) {
            assertTrue(expected.contains(current_test));
        }
    }


}
