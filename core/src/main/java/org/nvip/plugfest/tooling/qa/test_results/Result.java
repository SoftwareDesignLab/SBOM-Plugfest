package org.nvip.plugfest.tooling.qa.test_results;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;

/**
 * File: Result.java
 * JSON-Friendly storage for the result of a Test
 *
 * @author Derek Garcia
 */
public class Result {
    private final String testName;

    @JsonProperty("pass")
    private final boolean pass;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("additionalInfo")
    private HashMap<String, String> additionalInfo;     // optional field to hold any additional info


    /**
     * Create new result of a test
     *
     * @param testName name of the parent test
     * @param pass Whether the test passed or not
     * @param message test message
     */
    public Result(String testName, boolean pass, String message){
        this.testName = testName;
        this.pass = pass;
        this.message = message;
    }


    /**
     * Update additional info field with extra data
     *
     * @param key key for table
     * @param value value for key
     */
    public void updateInfo(String key, String value){
        // create additional info field if it doesn't exist
        if(this.additionalInfo == null)
            this.additionalInfo = new HashMap<>();

        // add to table
        this.additionalInfo.put(key, value);
    }

    public String getTestName() {
        return this.testName;
    }
}
