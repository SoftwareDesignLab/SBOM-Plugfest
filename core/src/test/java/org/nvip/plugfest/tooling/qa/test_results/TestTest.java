package org.nvip.plugfest.tooling.qa.test_results;

import static org.junit.jupiter.api.Assertions.*;

public class TestTest {

    /**
     * Test Constants
     */
    private static final String TEST_PASS_MESSAGE = "This test has passed.";

    /**
     * Test Constructor Test
     */

    @org.junit.jupiter.api.Test
    public void create_Test_test() {
        Test test_test = new Test(true, TEST_PASS_MESSAGE);
        assertNotNull(test_test);
    }

    @org.junit.jupiter.api.Test
    public void Test_getStatus_test() {
        Test test_test = new Test(true, TEST_PASS_MESSAGE);
        assertNotNull(test_test);
        assertTrue(test_test.getStatus());
    }

    @org.junit.jupiter.api.Test
    public void Test_getMessage_test() {
        Test test_test = new Test(true, TEST_PASS_MESSAGE);
        assertNotNull(test_test);
        assertEquals(TEST_PASS_MESSAGE, test_test.getMessage());
    }

}
