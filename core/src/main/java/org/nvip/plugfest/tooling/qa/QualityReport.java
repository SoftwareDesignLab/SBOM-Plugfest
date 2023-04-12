package org.nvip.plugfest.tooling.qa;

import org.nvip.plugfest.tooling.qa.test_results.TestResults;

import java.util.ArrayList;

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

    /**
     * Add results from a TestResults instance to the QualityReport.
     *
     * @param testResults The TestResults to add
     */
    public void addTestResult(TestResults testResults) {
        this.testResults.add(testResults);
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

    /**
     * Get total number of passed components in the quality report.
     *
     * @return Total number of passed components in the quality report
     */
    public int getPassedComponents() {
        int passed = 0;
        for(TestResults tr : testResults) {
            if(tr.isSuccessful())
                passed++;
        }
        return passed;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QualityReport{");
        sb.append("serialNumber=");
        sb.append(this.serialNumber);
        sb.append(",\n\n");
        sb.append(String.format("TEST RESULTS - TOTAL COMPONENTS PASSED: %d/%d\n\n", getPassedComponents(), testResults.size()));

        // Loop through all TestResults in the QualityReport
        for(TestResults result : testResults) {
            sb.append(result.toString());
        }

        sb.append("]}");

        return sb.toString();
    }
}