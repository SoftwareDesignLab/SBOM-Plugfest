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
import org.nvip.plugfest.tooling.sbom.Component;

import java.util.*;

/**
 * file: HasLicenseDataTest.java
 *
 * Test each component in the given SBOM if they have license data
 * @author Matthew Morrison
 * @author Derek Garcia
 */
public class HasLicenseDataTest extends MetricTest{

    private static final String TEST_NAME = "HasLicenseData";

    /**
     * Test all SBOM components for their licenses and if their data is present
     * (url, id, name, etc)
     * @param sbom SBOM to test
     * @return Collection of results for each component
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // create a list to hold each result of sbom components
        List<Result> results = new ArrayList<>();

        // Check for components
        for(Component c : sbom.getAllComponents()){
            Result r;
            Set<String> licenses = c.getLicenses();
            // Check if licenses exist
            // licenses are not present
            if(isEmptyOrNull(licenses)){
                r = new Result(TEST_NAME, Result.STATUS.FAIL,
                        "No Licenses Found");
                r.updateInfo(Result.Context.STRING_VALUE,
                        "No Licenses Found for Component");
            }
            // licenses are present
            else{
                r = new Result(TEST_NAME, Result.STATUS.PASS,
                        c.getLicenses().size() + " Licenses Found");
                String licenseList = String.join(", ", licenses);
                r.updateInfo(Result.Context.STRING_VALUE,
                        "Licenses: " + licenseList);
            }

            r.addContext(c, "licenses");
            r.updateInfo(Result.Context.FIELD_NAME, "licenses");
            results.add(r);
        }

        return results;
    }

}
