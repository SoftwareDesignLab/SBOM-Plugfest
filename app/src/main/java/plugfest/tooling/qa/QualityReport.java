package plugfest.tooling.qa;

import java.util.HashMap;
import java.util.Map;

public class QualityReport {
    private final Map<String, String> testResults;

    // include data on what tests were run
    public QualityReport(){
        testResults = new HashMap<>();
    }

    public void addTestResult(String testName, String testResult) {
        // Insert the new test result
        final String existingValue = this.testResults.put(testName, testResult);

        // If existingValue is not null, this action overrode that value
        if(existingValue != null)
            System.out.printf("Test %s already existed with value %s and has been overridden with value %s.", testName, existingValue, testResult);
    }
}
