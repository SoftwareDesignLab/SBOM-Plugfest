package org.nvip.plugfest.tooling.qa.test_results;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.*;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestResultsTest {

    /**
     * Test Constants
     */

    private static final String TEST_MESSAGE = "This is a test.";

    private static final String TEST_MESSAGE_TWO = "This is also a test.";

    private static final String TO_STRING_PASS_MESSAGE = "PASSED: " + TEST_MESSAGE;

    private static final String TO_STRING_FAIL_MESSAGE = "FAILED: " + TEST_MESSAGE;

    /**
     * Test Variables
     */

    Component test_component_a;

    Component test_component_b;

    org.nvip.plugfest.tooling.qa.test_results.Test test_test_a;

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
     * TestResults Constructor Test
     */

    @Test
    public void create_TestResults_test() {
        TestResults test_tr = new TestResults(test_component_a);
        assertNotNull(test_tr);
    }

}
