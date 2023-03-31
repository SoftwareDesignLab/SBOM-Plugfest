package plugfest.tooling.qa;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class QualityReport {
    private final Map<String, ArrayList<String>> testResults;
    private final String serialNumber;

    /**
     * Create new QualityReport object with the SBOM serialNumber.
     * @param serialNumber the SBOM serialNumber
     */
    public QualityReport(String serialNumber){
        this.testResults = new HashMap<>();
        this.serialNumber = serialNumber;
    }

    /**
     * Create new QualityReport object without a serialNumber.
     * This object should be intended to be combined with another
     * that does have a valid serialNumber.
     */
    public QualityReport(){
        this.testResults = new HashMap<>();
        this.serialNumber = "INVALID_SN";
    }

    public void addTestResult(String testName, ArrayList<String> testResults) {
        // Insert the new test result
        final ArrayList<String> existingValue = this.testResults.put(testName, testResults);

        // If existingValue is not null, this action overrode that value
        if(existingValue != null)
            System.out.printf("Test %s already existed with value %s and has been overridden with value %s.", testName, existingValue, testResults);
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("QualityReport{");
        sb.append("serialNumber=");
        sb.append(this.serialNumber);
        sb.append(",\n");
        sb.append("testResults=[\n  ");

        final String[] keySet = this.testResults.keySet().toArray(new String[0]);
        for (int i = 0; i < keySet.length; i++) {
            final String key = keySet[i];
            sb.append(key);
            sb.append("\n    ");
            final ArrayList<String> results = this.testResults.get(key);
            sb.append(String.join(",\n    ", results));
            if(i + 1 < keySet.length) sb.append("\n  ");
        }

        sb.append("]}");

        return sb.toString();
    }
}
