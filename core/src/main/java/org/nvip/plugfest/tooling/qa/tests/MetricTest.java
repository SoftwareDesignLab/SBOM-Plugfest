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

import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * file: MetricTest.java
 *
 * Template for MetricTests
 *
 * @author Derek Garcia
 */
public abstract class MetricTest {

    /**
     * Utility function that checks if an object is null, a string is blank or a collection is empty
     *
     * todo maybe remove?
     * @param o Object to test
     * @return true if empty, false otherwise
     */
    protected boolean isEmptyOrNull(Object o){
        // Object is null
        if(o == null)
            return true;

        // Check for empty string
        if(o instanceof String)
            return o.equals("");

        // Check for empty collection
        if(o instanceof Collection<?>)
            return ((Collection<?>) o).isEmpty();

        // Object that isn't null
        return false;
    }

    /**
     * Test the given SBOM
     *
     * @param sbom SBOM to test
     * @return Collection of Results
     */
    public abstract List<Result> test(SBOM sbom);
}
