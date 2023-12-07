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

import java.util.ArrayList;
import java.util.List;

/**
 * file: HasBomVersionTest.java
 *
 * For a given SBOM, check if it has a version number declared
 * @author Matthew Morrison
 */
public class HasBomVersionTest extends MetricTest{
    private static final String TEST_NAME = "HasBomVersion";

    /**
     * Given an SBOM, test if is it contains a version number
     * @param sbom SBOM to test
     * @return the result of checking the sbom's version number
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> result = new ArrayList<>();
        Result r;

        // if sbom version is empty or null, the sbom is missing this
        // info, test fails
        if(isEmptyOrNull(sbom.getSbomVersion())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "SBOM " +
                    "does not have version number declared");
            r.updateInfo(Result.Context.STRING_VALUE, "SBOM Version Number " +
                    "is Missing");
        }
        // sbom version is present, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "SBOM " +
                    "has a version number declared");
            r.updateInfo(Result.Context.STRING_VALUE, sbom.getSbomVersion());
        }
        r.addContext(sbom,"SBOM Version Number");
        r.updateInfo(Result.Context.FIELD_NAME, "version");
        result.add(r);

        return result;
    }
}
