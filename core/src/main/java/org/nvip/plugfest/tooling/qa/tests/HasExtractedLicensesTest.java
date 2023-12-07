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

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * file: HasCreationInfoTest.java
 *
 * For an SPDX SBOM, test components to see if any contain extracted
 * licenses not on the SPDX license list and list them
 * @author Matthew Morrison
 */
public class HasExtractedLicensesTest extends MetricTest{
    public static final String TEST_NAME = "HasExtractedLicenses";

    /**
     * Check all components in a given SBOM for extracted licenses not on
     * the SPDX license list
     * @param sbom SBOM to test
     * @return a collection of results if any extracted licenses exist
     * in the SBOM
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();
        Result r;

        // for every component, check for any extracted licenses
        for(Component c : sbom.getAllComponents()){
            Map<String, Map<String, String>>  extractedLicenses = c.getExtractedLicenses();
            // skip if no extracted licenses are found for the component
            if(!extractedLicenses.isEmpty()){
                ArrayList<String> licenseStrings = new ArrayList<>();
                for(String licenseRef : extractedLicenses.keySet()){{
                    licenseStrings.add(licenseRef);
                }}
                    String message = "Extracted Licenses Found:  "
                            + String.join(", ", licenseStrings);
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "Extracted licenses found for component "
                            + c.getName());
                    r.addContext(c, "Extracted Licenses");
                    r.updateInfo(Result.Context.FIELD_NAME, "ExtractedLicenses");
                    r.updateInfo(Result.Context.STRING_VALUE, message);
                    results.add(r);

            }
        }

        if(results.isEmpty()){
            r = new Result(TEST_NAME, Result.STATUS.PASS, "No Extracted " +
                    "Licenses found in SBOM");
            r.addContext(sbom, "Extracted Licenses");
            r.updateInfo(Result.Context.FIELD_NAME, "ExtractedLicenses");
            r.updateInfo(Result.Context.STRING_VALUE,
                    "No Extracted Licenses Found in SBOM");
            results.add(r);
        }

        return results;
    }
}
