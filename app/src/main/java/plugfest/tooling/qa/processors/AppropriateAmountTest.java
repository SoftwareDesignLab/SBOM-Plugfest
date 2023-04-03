package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.test_results.Test;
import plugfest.tooling.qa.test_results.TestResults;
import plugfest.tooling.sbom.Component;

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
    public TestResults test(Component c) {
        // Init StringBuilder
        TestResults testResults = new TestResults(c);

        // Test publisher name
        testResults.addTest(testPublisherName(c));

        // Test component name
        testResults.addTest(testComponentName(c));

        // Test component version
        testResults.addTest(testComponentVersion(c));

        // Return result
        return testResults;
    }

    private Test testPublisherName(Component c) {
        if(c.getPublisher() != null &&
                c.getPublisher().strip().length() > maxLineLength) {
            return new Test(false, "Publisher Name Length > 80");
        }

        return new Test(true, "Publisher Name <= 80");
    }

    private Test testComponentName(Component c) {
        if(c.getName() != null &&
                c.getName().strip().length() > maxLineLength) {
            return new Test(false, "Component Name Length > 80");
        }
        return new Test(true, "Component Name Length <= 80");
    }

    private Test testComponentVersion(Component c) {
        if(c.getVersion() != null &&
                c.getVersion().strip().length() > maxLineLength) {
            new Test(false, "Version Length > 80");
        }
        return new Test(true, "Version Length <= 80");
    }
}
