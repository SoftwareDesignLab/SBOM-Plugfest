package org.nvip.plugfest.tooling.qa.test_results;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.HashMap;

/**
 * File: Result.java
 * JSON-Friendly storage for the result of a Test
 *
 * @author Derek Garcia
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result {

    // Status utility enums
    public enum STATUS {
        PASS(1),
        FAIL(0),
        ERROR(-1);

        private final int code;

        STATUS(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    public enum Context{
        TYPE,
        FIELD_NAME,
        STRING_VALUE,
        IDENTIFIER
    }

    @JsonIgnore
    private final String testName;
    @JsonProperty("pass")
    private final int pass;

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
    public Result(String testName, STATUS pass, String message){
        this.testName = testName;
        this.pass = pass.getCode();
        this.message = message;
    }
    public void updateInfo(Context context, String value){
        if(this.additionalInfo == null)
            this.additionalInfo = new HashMap<>();
        this.additionalInfo.put(context.toString(), value);
    }

    public void addContext(Object o, String fieldName){
        if(o instanceof SBOM){
            updateInfo(Context.TYPE, "SBOM");
            updateInfo(Context.IDENTIFIER, ((SBOM) o).getSerialNumber());
        }

        if(o instanceof Component){
            updateInfo(Context.TYPE, "Component");
            updateInfo(Context.IDENTIFIER, ((Component) o).getName());
        }

        updateInfo(Context.FIELD_NAME, fieldName);
    }

    ///
    /// Getters
    ///

    public String getTestName() {
        return this.testName;
    }
}
