package org.nvip.plugfest.tooling.qa.test_results;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TestTest {

    /**
     * Test Constants
     */
    private static final String test_pass_message = "This test has passed.";

    /**
     * Test Constructor Test
     */

    @org.junit.jupiter.api.Test
    public void create_Test_test() {
        Test test_test = new Test(true, test_pass_message);
        assertNotNull(test_test);
    }

}
