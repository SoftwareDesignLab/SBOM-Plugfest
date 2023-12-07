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

package org.nvip.plugfest.tooling.qa;

import com.fasterxml.jackson.annotation.*;
import org.nvip.plugfest.tooling.qa.tests.Result;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * JSON-friendly object to report Metric findings
 *
 * @author Dylan Mulligan
 * @author Matt London
 * @author Ian Dunn
 * @author Derek Garcia
 */
@JsonPropertyOrder({"uid", "attributeResults" })
public class QualityReport {
    @JsonProperty
    private final String uid;
    @JsonProperty
    private final HashMap<String, HashMap<String, ArrayList<Result>>> attributeResults = new HashMap<>();


    /**
     * Create new QualityReport object with a form of UID
     *
     * @param uid unique identifier for the quality report
     */
    public QualityReport(String uid){
        this.uid = uid;
    }

    /**
     * Update an attribute with result details
     *
     * @param attributeName Name of attribute
     * @param results List of test results
     */
    public void updateAttribute(String attributeName, List<Result> results){
        // init test results
        HashMap<String, ArrayList<Result>> testResults =
                this.attributeResults.computeIfAbsent(attributeName, k -> new HashMap<>());

        // Add each test and result to the attribute
        for(Result r : results){
            testResults.computeIfAbsent(r.getTestName(), k -> new ArrayList<>());
            testResults.get(r.getTestName()).add(r);
        }
    }
}
