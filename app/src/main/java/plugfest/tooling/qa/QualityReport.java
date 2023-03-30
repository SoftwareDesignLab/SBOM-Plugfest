package plugfest.tooling.qa;

import java.util.HashMap;
import java.util.Map;

public class QualityReport {
    private final Map<String, String> testResults;
    private final String serialNumber;

    // include data on what tests were run
    public QualityReport(String serialNumber){
        this.testResults = new HashMap<>();
        this.serialNumber = serialNumber;
    }

    public void addTestResult(String testName, String testResult) {
        // Insert the new test result
        final String existingValue = this.testResults.put(testName, testResult);

        // If existingValue is not null, this action overrode that value
        if(existingValue != null)
            System.out.printf("Test %s already existed with value %s and has been overridden with value %s.", testName, existingValue, testResult);
    }

    /**
     * Append another QualityReport object to this one. This
     * adds other.testResults to this.testResults (absorbing
     * the other object's data).
     *
     * @param other QualityReport object to be appended
     */
    public void append(QualityReport other) {
        this.testResults.putAll(other.testResults);
    }
}
