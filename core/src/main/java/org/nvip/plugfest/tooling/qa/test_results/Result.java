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
    @JsonProperty("type")
    private final String type;

    @JsonProperty("pass")
    private final boolean pass;

    @JsonProperty("message")
    private final String message;

    @JsonProperty("additionalInfo")
    private HashMap<String, String> additionalInfo;     // optional field to hold any additional info


    /**
     * Create new result of a test
     *
     * @param type Type of the parent processor
     * @param pass Whether the test passed or not
     * @param message test message
     */
    public Result(String type, boolean pass, String message){
        this.type = type;
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
}
