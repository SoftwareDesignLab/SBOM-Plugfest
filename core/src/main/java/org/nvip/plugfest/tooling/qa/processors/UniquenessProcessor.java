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

import org.nvip.plugfest.tooling.qa.tests.AccurateCPETest;
import org.nvip.plugfest.tooling.qa.tests.AccuratePURLTest;
import org.nvip.plugfest.tooling.qa.tests.HasHashDataTest;
import org.nvip.plugfest.tooling.qa.tests.ValidHashDataTest;

/**
 * file: UniquenessProcessor.java
 *
 * Collection of tests to ensure component's UIDs are accurate
 * @author Matthew Morrison
 */
public class UniquenessProcessor extends AttributeProcessor {

    /**
     * Create a new preset collection of tests
     */
    public UniquenessProcessor(){
        this.attributeName = "Uniqueness";
        this.metricTests.add(new HasHashDataTest());
        this.metricTests.add(new ValidHashDataTest());
        this.metricTests.add(new AccuratePURLTest());
        this.metricTests.add(new AccurateCPETest());
        /*
        TODO
         accurateSWID: SWID matches stored swid data
         */
    }
}
