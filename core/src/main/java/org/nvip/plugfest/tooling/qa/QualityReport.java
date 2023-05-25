package org.nvip.plugfest.tooling.qa;

import com.fasterxml.jackson.annotation.JsonProperty;
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
public class QualityReport {

    private class AttributeResults {
        @JsonProperty("attributeName")
        private final String attributeName;

        @JsonProperty("attributeResults")
        private final List<TestResults> testResults = new ArrayList<>();

        public AttributeResults(String attributeName){
            this.attributeName = attributeName;
        }

    }

    private class TestResults {
        @JsonProperty("testName")
        private final String testName;

        @JsonProperty("testResults")
        private final HashMap<String, ArrayList<Result>> testResults = new HashMap<>();

        public TestResults(String testName){
            this.testName = testName;
        }

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

    @JsonProperty("uid")
    private final String uid;

    @JsonProperty("attributeTests")
    private final List<AttributeResults> attributeResults = new ArrayList<>();



    /**
     * Create new QualityReport object with the SBOM serialNumber.
     *
     * @param uid unique identifier for the quality report
     */
    public QualityReport(String uid){
        this.uid = uid;
    }


    public void addAttribute(String attribute, List<Result> results){

    }






}
