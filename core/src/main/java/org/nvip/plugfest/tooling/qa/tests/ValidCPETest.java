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

import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.uids.CPE;

import java.util.ArrayList;
import java.util.List;

/**
 * file: ValidCPETest.java
 *
 * Tests if the cpes are valid
 *
 * @author Derek Garcia
 */
public class ValidCPETest extends MetricTest {
    private static final String TEST_NAME = "ValidCPE";

    /**
     * Validates the CPE
     *
     * @param sbom SBOM to test
     * @return Collection of results
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();

        for (Component c : sbom.getAllComponents()) {
            // Skip if no CPEs
            if(isEmptyOrNull(c.getCpes()))
                continue;
            Result r;

            // Else attempt to make object
            for (String cpe : c.getCpes()) {
                // Attempt to make new Purl Object
                try{
                    new CPE(cpe);    // throws error if given purl string is invalid
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "Valid CPE String");
                } catch (Exception e){
                    Debug.log(Debug.LOG_TYPE.WARN, "Failed to parse CPE \"" + cpe +"\" | " + e.getMessage());    // log incase regex fails
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Invalid CPE String");
                }
                r.addContext(c, "Valid CPE String");
                r.updateInfo(Result.Context.FIELD_NAME, "cpe");
                r.updateInfo(Result.Context.STRING_VALUE, cpe);
                results.add(r);
            }
        }

        // return findings
        return results;
    }
}
