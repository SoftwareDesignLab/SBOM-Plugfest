package org.nvip.plugfest.tooling.qa;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.qa.test_results.TestResults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

/**
 * QualityReport is a collection of TestResults objects that relate to a particular SBOM
 *
 * @author Dylan Mulligan
 * @author Matt London
 * @author Ian Dunn
 * @author Derek Garcia
 */
public class QualityReport {

    @JsonProperty("uid")
    private final String uid;
    @JsonProperty("tests")
    private final HashMap<String, ArrayList<Result>> testResults = new HashMap<>();


    /**
     * Create new QualityReport object with the SBOM serialNumber.
     *
     * @param uid unique identifier for the quality report
     */
    public QualityReport(String uid){
        this.uid = uid;
    }

    public void massUpdate(String testName, Set<Result> results){
        for(Result r : results)
            update(testName, r);
    }

    private void update(String testName, Result result){
        // Create new array for new test
        this.testResults.computeIfAbsent(testName, k -> new ArrayList<>());
        // update results
        this.testResults.get(testName).add(result);
    }
}
