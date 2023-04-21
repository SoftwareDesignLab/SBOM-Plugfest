package org.nvip.plugfest.tooling.qa.test_results;

import static org.junit.jupiter.api.Assertions.*;

public class TestTest {

    /**
     * Test Constants
     */
    private static final String TEST_MESSAGE = "This is a test.";

    private static final String TO_STRING_PASS_MESSAGE = "PASSED: " + TEST_MESSAGE;

    private static final String TO_STRING_FAIL_MESSAGE = "FAILED: " + TEST_MESSAGE;

    /**
     * Test Constructor Test
     */

    @org.junit.jupiter.api.Test
    public void create_Test_test() {
        Test test_test = new Test(true, TEST_MESSAGE);
        assertNotNull(test_test);
    }

    @org.junit.jupiter.api.Test
    public void Test_getStatus_test() {
        Test test_test = new Test(true, TEST_MESSAGE);
        assertNotNull(test_test);
        assertTrue(test_test.getStatus());
    }

    @org.junit.jupiter.api.Test
    public void Test_getMessage_test() {
        Test test_test = new Test(true, TEST_MESSAGE);
        assertNotNull(test_test);
        assertEquals(TEST_MESSAGE, test_test.getMessage());
    }

    @org.junit.jupiter.api.Test
    public void Test_toString_pass_message_test() {
        Test test_test = new Test(true, TEST_MESSAGE);
        assertNotNull(test_test);
        String toString_result = test_test.toString();
        assertEquals(TO_STRING_PASS_MESSAGE, toString_result);
    }
    
    @org.junit.jupiter.api.Test
    public void Test_toString_fail_message_test() {
        Test test_test = new Test(false, TEST_MESSAGE);
        assertNotNull(test_test);
        String toString_result = test_test.toString();
        assertEquals(TO_STRING_FAIL_MESSAGE, toString_result);
    }

}
