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

/**
 * file: HasSPDXIDTest.java
 *
 * For every component in a SPDX SBOM, check that each component has a
 * valid SPDXID (SPDXID: "SPDXRef-[idstring]")
 * @author Matthew Morrison
 */
public class HasSPDXIDTest extends MetricTest{
    public static final String TEST_NAME = "HasSPDXID";

    /**
     * Test every component in a given SBOM for a valid SPDXID
     * @param sbom SBOM to test
     * @return a collection of results for every component in the SBOM
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();

        //TODO UID's (SPDXID) in metadata not present
        // Check that the metadata contains a valid SPDXID


        //for every component, check for SPDXID in HasSPDXID
        for(Component c : sbom.getAllComponents()){
            results.add(HasSPDXIDComponent(c));
        }
        return results;
    }

    /**
     * Check a single component for an SPDXID and test that it matches the
     * format "SPDXRef-..."
     * @param c the component to test
     * @return a result of if a component has an SPDXID and if it is valid
     */
    public Result HasSPDXIDComponent(Component c){
        // check that the component has an SPDXID
        String spdxID = c.getUniqueID();
        Result r;

        // SPDXID is not present, test fails
        if(isEmptyOrNull(spdxID)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component does " +
                    "not contain a SPDXID");
            r.updateInfo(Result.Context.STRING_VALUE, "SPDXID is Missing");
        }
        // SPDXID is present, continue test
        else{
            // TODO Can we make this more thorough? Not just format?
            // check that SPDXID is a valid format
            // SPDXID starts with a valid format, test passes
            if(spdxID.startsWith("SPDXRef-")){
                r = new Result(TEST_NAME, Result.STATUS.PASS, "Component has " +
                        "a valid SPDXID");
                r.updateInfo(Result.Context.STRING_VALUE, spdxID);
            }
            // SPDX starts with an invalid format, test fails
            else{
                r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component has " +
                        "an invalid SPDXID format");
                r.updateInfo(Result.Context.STRING_VALUE,
                        "SPDXID does not start with \"SPDXRef-\". " +
                                "SPDXID Found: " + spdxID);
            }
            // add context when a SPDXID is present
            r.updateInfo(Result.Context.FIELD_NAME, "SPDXID");
        }
        r.addContext(c, "SPDXID");
        return r;
    }
}
