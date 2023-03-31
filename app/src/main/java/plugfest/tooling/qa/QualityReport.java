package plugfest.tooling.qa;

import plugfest.tooling.qa.test_results.TestResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QualityReport {
    private final ArrayList<TestResults> testResults;
    private final String serialNumber;

    /**
     * Create new QualityReport object with the SBOM serialNumber.
     * @param serialNumber the SBOM serialNumber
     */
    public QualityReport(String serialNumber){
        this.testResults = new ArrayList<>();
        this.serialNumber = serialNumber;
    }

    /**
     * Create new QualityReport object without a serialNumber.
     * This object should be intended to be combined with another
     * that does have a valid serialNumber.
     */
    public QualityReport(){
        this.testResults = new ArrayList<>();
        this.serialNumber = "INVALID_SN";
    }

    public void addTestResult(String testName, TestResults testResults) {
        // Insert the new test result
        final boolean existingValue = this.testResults.add(testResults);

        // TODO figure this out
//        // If existingValue is false, this action overrode that value
//        if(!existingValue)
//            System.out.printf("Test %s already existed with value %s and has been overridden with value %s.", testName, existingValue, testResults);
    }

    /**
     * Append another QualityReport object to this one. This
     * adds other.testResults to this.testResults (absorbing
     * the other object's data).
     *
     * @param other QualityReport object to be appended
     */
    public void append(QualityReport other) {
        this.testResults.addAll(other.testResults);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QualityReport{");
        sb.append("serialNumber=");
        sb.append(this.serialNumber);
        sb.append(",\n");
        sb.append("testResults=[\n  ");

        for(TestResults result : testResults) {
            sb.append(result.toString());
        }

        sb.append("]}");

        return sb.toString();
    }
}
