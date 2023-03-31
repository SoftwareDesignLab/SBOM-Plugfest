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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QualityReport{");
        sb.append("serialNumber=");
        sb.append(this.serialNumber);
        sb.append(",\n");
        sb.append("testResults=[\n  ");

        // Loop through all TestResults in the QualityReport
        for(TestResults result : testResults) {
            sb.append(result.toString());
        }

        sb.append("]}");

        return sb.toString();
    }
}
