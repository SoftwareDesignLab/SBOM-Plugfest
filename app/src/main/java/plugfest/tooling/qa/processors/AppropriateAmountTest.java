package plugfest.tooling.qa.processors;

import plugfest.tooling.sbom.Component;

import java.util.ArrayList;

/**
 * File: AppropriateAmountTest.java
 * Tests a component to check if each attribute is <= 80 characters in length, the limit for max line length in Java.
 *
 * @author Ian Dunn
 */
public class AppropriateAmountTest extends MetricTest {

    /**
     * Current maximum line length each attribute is being tested on.
     */
    private final int maxLineLength;

    /**
     * Create new ApproprateAmountTest and specify maximum line length.
     */
    protected AppropriateAmountTest() {
        super("Appropriate Amount Test");

        // Set maximum line length to 80
        this.maxLineLength = 80;
    }

    /**
     * Test a component to check its attributes' maximum line lengths.
     *
     * @param c The component to test
     * @return ArrayList of all generated test results
     */
    @Override
    public ArrayList<String> test(Component c) {
        // Init StringBuilder
        final ArrayList<String> testResults = new ArrayList<>();
        final String UUIDShort = c.getUUID().toString().substring(0, 5);

        /*
            Component publisher name length <= 80 chars
         */
        if(c.getPublisher() != null &&
                c.getPublisher().strip().length() > maxLineLength) {
            testResults.add(String.format("FAILED: Component %s Publisher Name Length > 80", UUIDShort));
        }

        /*
            Component name length <= 80 chars
         */
        if(c.getName() != null &&
                c.getName().strip().length() > maxLineLength) {
            // Add separator if not first check to fail
            if(!testResults.isEmpty()) testResults.add("\n");
            testResults.add(String.format("FAILED: Component %s Name Length > 80", UUIDShort));
        }

        /*
            Component version length <= 80 chars
         */
        if(c.getVersion() != null &&
                c.getVersion().strip().length() > maxLineLength) {
            // Add separator if not first check to fail
            if(!testResults.isEmpty()) testResults.add("\n");
            testResults.add(String.format("FAILED: Component %s Version Length > 80", UUIDShort));
        }

        // If no checks failed, mark test as passed
        if(testResults.isEmpty()) testResults.add("PASSED");

        // Return result
        return testResults;
    }
}
