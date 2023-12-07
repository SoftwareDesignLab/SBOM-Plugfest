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

package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.tests.MetricTest;
import org.nvip.plugfest.tooling.qa.tests.Result;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

/**
 * file: AttributeProcessor.java
 *
 * Template for AttributeProcessors
 *
 * @author Derek Garcia
 */
public abstract class AttributeProcessor {

    protected List<MetricTest> metricTests = new ArrayList<>();
    protected String attributeName;

    /**
     * Default constructor for prebuilt attribute processors
     */
    protected AttributeProcessor(){}

    /**
     * Constructor for custom processors
     *
     * @param attributeName Name of processor
     * @param metricTests Collection of tests to run
     */
    protected AttributeProcessor(String attributeName, List<MetricTest> metricTests){
        this.attributeName = attributeName;
        this.metricTests = metricTests;
    }


    /**
     * Run tests against given SBOM
     *
     * @param sbom sbom to test
     * @return Collection of test results
     */
    public List<Result> process(SBOM sbom){
        List<Result> results = new ArrayList<>();
        // run each test
        for(MetricTest test : this.metricTests)
            results.addAll(test.test(sbom));

        return results;
    }

    ///
    /// Getters
    ///

    public String getAttributeName() {
        return this.attributeName;
    }
}
