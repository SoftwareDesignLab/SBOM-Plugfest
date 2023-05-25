package org.nvip.plugfest.tooling.qa;

import com.fasterxml.jackson.annotation.*;
import org.nvip.plugfest.tooling.qa.test_results.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * QualityReport is a collection of TestResults objects that relate to a particular SBOM
 *
 * @author Dylan Mulligan
 * @author Matt London
 * @author Ian Dunn
 * @author Derek Garcia
 */
@JsonPropertyOrder({"uid", "attributeResults" })
public class QualityReport {


    private class TestResults {

        @JsonProperty("testResults")
        private final HashMap<String, ArrayList<Result>> testResults = new HashMap<>();

//        public TestResults(String testName){
//            this.testName = testName;
//        }

        public void massUpdate(String testName, List<Result> results){
            for(Result r : results){
                // Create new array for new test
                this.testResults.computeIfAbsent(testName, k -> new ArrayList<>());
                // update results
//                this.testResults.get(testName).add(result);
            }
//            update(testName, r);
        }

        private void update(String testName, Result result){

        }

    }


    @JsonProperty
    private final String uid;

    @JsonProperty
    private final HashMap<String, HashMap<String, ArrayList<Result>>> attributeResults = new HashMap<>();


    /**
     * Create new QualityReport object with the SBOM serialNumber.
     *
     * @param uid unique identifier for the quality report
     */
    public QualityReport(String uid){
        this.uid = uid;
    }


    public void updateAttribute(String attributeName, List<Result> results){
        // init test results
        HashMap<String, ArrayList<Result>> testResults =
                this.attributeResults.computeIfAbsent(attributeName, k -> new HashMap<>());

        for(Result r : results){
            testResults.computeIfAbsent(r.getTestName(), k -> new ArrayList<>());
            testResults.get(r.getTestName()).add(r);
        }

    }

    //    private static void updateResultMap(HashMap<String, Object> resultMap, String key, Object value){
//        resultMap.computeIfAbsent(key, k -> new ArrayList<>());
//        resultMap.get(key)(value);
//    }






}
