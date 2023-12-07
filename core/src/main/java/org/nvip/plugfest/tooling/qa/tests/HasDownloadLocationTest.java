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
 * file: HasDownloadLocationTest.java
 *
 * For an SPDX SBOM, test that all components contains
 * a download location
 * @author Matthew Morrison
 */
public class HasDownloadLocationTest extends MetricTest{
    private static final String TEST_NAME = "HasDownloadLocation";

    /**
     * Test every component in the sbom for the PackageDownloadLocation field
     * and that it has a value
     * @param sbom SBOM to test
     * @return a collection of results from each component and if it contains
     * info about its download location
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();

        // for every component, test for its download location
        for(Component c : sbom.getAllComponents()){
            results.add(componentDownloadLocation(c));
        }
        return results;
    }

    /**
     * Test a single component for its download location and if its present
     * @param c the component to test
     * @return a result of if the component contains info about its
     * download location
     */
    private Result componentDownloadLocation(Component c){
        Result r;

        // TODO check for NOASSERTION or NONE?
        // if the downloadLocation is null, the component did not
        // include it, test fails
        if(isEmptyOrNull(c.getDownloadLocation())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component did " +
                    "not include download location");
            r.updateInfo(Result.Context.STRING_VALUE,
                    "Download Location is Missing");
        }
        // downloadLocation has a value, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "Component " +
                    "included download location");
            r.updateInfo(Result.Context.STRING_VALUE, c.getDownloadLocation());
        }
        r.addContext(c, "PackageDownloadLocation");
        r.updateInfo(Result.Context.FIELD_NAME, "PackageDownloadLocation");
        return r;
    }
}
