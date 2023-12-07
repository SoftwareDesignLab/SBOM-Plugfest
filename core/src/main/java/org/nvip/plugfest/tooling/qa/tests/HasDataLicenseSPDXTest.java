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
import java.util.Map;

/**
 * file: HasDataLicenseSPDXTest.java
 *
 * For an SPDX SBOM, test that the SBOM Metadata contains the
 * DataLicense field with a license of CC0-1.0
 * @author Matthew Morrison
 */
public class HasDataLicenseSPDXTest extends MetricTest{

    private static final String TEST_NAME = "HasDataLicenseSPDX";

    /**
     * Test the SPDX SBOM Metadata to see if it contains a data license of
     * CC0-1.0
     * @param sbom SBOM to test
     * @return The result of checking for the SBOM's data license
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> result = new ArrayList<>();
        Result r;

        // get the metadata from the sbom
        Map<String, String> metadata = sbom.getMetadata();

        // if metadata is present in the SBOM
        if(metadata.size() >= 1){
            // continue to hasDataLicense and get a result
            r = hasDataLicense(metadata);
            r.addContext(sbom, "DataLicense");
            result.add(r);
        }
        // metadata is not present in the SBOM, test automatically fails
        else{
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "SBOM does not " +
                    "contain metadata, so no DataLicense included in SBOM");
            r.addContext(sbom, "Metadata DataLicense");
            r.updateInfo(Result.Context.STRING_VALUE,
                    "No DataLicense is in SBOM");
            result.add(r);
        }
        return result;
    }

    /**
     * Helper function to check for Data License in SBOM when metadata is
     * confirmed to be present
     * @param metadata the map of values in the SBOM's metadata
     * @return the result of if the metadata contains the correct
     * DataLicense of CC0-1.0
     */
    private Result hasDataLicense(Map<String, String> metadata){
        // the DataLicense that is expected
        String license = "CC0-1.0";
        Result r;

        // if the data license is within the metadata, continue the test
        if(metadata.containsKey("datalicense")){
            // extract the value of the data license in metadata
            String DataLicense = metadata.get("datalicense");

            // if the DataLicense contains the CC0-1.0
            if(DataLicense.contains(license)){
                r = new Result(TEST_NAME, Result.STATUS.PASS, "DataLicense " +
                        "is present and is CC0-1.0");
            }
            else{
                r = new Result(TEST_NAME, Result.STATUS.FAIL, "DataLicense " +
                        "is present but is not CC0-1.0");
            }
            r.updateInfo(Result.Context.FIELD_NAME, "DataLicense");
            r.updateInfo(Result.Context.STRING_VALUE, DataLicense);
        }
        // data license is not in the metadata, test fails
        else{
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Metadata does " +
                    "not contain a DataLicense");
            r.updateInfo(Result.Context.FIELD_NAME, "DataLicense");
            r.updateInfo(Result.Context.STRING_VALUE, "DataLicense is Missing");
        }
        return r;
    }
}
