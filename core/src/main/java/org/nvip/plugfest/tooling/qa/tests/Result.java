/ **
* Copyright 2021 Rochester Institute of Technology (RIT). Developed with
* government support under contract 70RCSA22C00000008 awarded by the United
* States Department of Homeland Security for Cybersecurity and Infrastructure Security Agency.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the “Software”), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* /

package org.nvip.plugfest.tooling.qa.tests;

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

    /**
     * Result test statuses
     */
    public enum STATUS {
        PASS(1),
        FAIL(0),
        ERROR(-1);

        /*
        Utilities to convert to ints
         */
        private final int code;
        STATUS(int code) {
            this.code = code;
        }

        public int getCode() {
            return this.code;
        }
    }

    /**
     * Enums to update the additional info section
     */
    public enum Context{
        TYPE,           // Type of test (component, sbom, etc)
        FIELD_NAME,     // Name of value being checked EX: "publisher"
        STRING_VALUE,   // String of value being checked EX: when checking publisher string_value="google"
        IDENTIFIER      // UID of the source
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

    /**
     * Update the additional Info section with data
     *
     * @param context Key context enum
     * @param value Value to set at that field
     */
    public void updateInfo(Context context, String value){
        // Create new hashmap if one doesn't exist
        if(this.additionalInfo == null)
            this.additionalInfo = new HashMap<>();
        // add value
        this.additionalInfo.put(context.toString(), value);
    }

    /**
     * Utility to add context based on the given object
     *
     * @param o Object to reference
     * @param fieldName name of field to update
     */
    public void addContext(Object o, String fieldName){
        // add SBOM context
        if(o instanceof SBOM){
            updateInfo(Context.TYPE, "SBOM");
            updateInfo(Context.IDENTIFIER, ((SBOM) o).getSerialNumber());
        }

        // add Component context
        if(o instanceof Component){
            updateInfo(Context.TYPE, "Component");
            updateInfo(Context.IDENTIFIER, ((Component) o).getName());  // todo better identifier?
        }

        // todo Other objects? ie Purl, CPE, Signatures, etc

        // add field info
        updateInfo(Context.FIELD_NAME, fieldName);
    }

    ///
    /// Getters
    ///

    public String getTestName() {
        return this.testName;
    }
}
