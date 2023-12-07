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
import java.util.Collection;
import java.util.List;
import java.util.Set;

/**
 * file: MinElementTest.java
 *
 * Tests for the 7 minimum data fields as stated by the NTIA
 * <a href="https://www.ntia.doc.gov/files/ntia/publications/sbom_minimum_elements_report.pdf">...</a>
 *
 * @author Derek Garcia
 */
public class MinElementTest extends MetricTest {
    private static final String TEST_NAME = "MinimumElements";

    /**
     * Test for minimum Elements
     *
     * @param sbom SBOM to test
     * @return List of results
     */
    @Override
    public List<Result> test(SBOM sbom) {

        // Test SBOM fields
        List<Result> results = new ArrayList<>(testSBOMFields(sbom));

        // Test Component Fields
        for(Component c : sbom.getAllComponents()){
            if(c.isUnpackaged()) continue;
            results.addAll(testComponentFields(c));
        }

        // return findings
        return results;
    }


    /**
     * Testing the data fields for the SBOM itself
     *
     * @param sbom SBOM to test
     * @return List of results
     */
    private List<Result> testSBOMFields(SBOM sbom){
        List<Result> results = new ArrayList<>();
        Result r;   // utility result for adding additional detail todo clunky solution, could be improved

        // Test supplier
        // "The name of the entity that creates the SBOM data for this component."
        r = resultEmptyOrNull(sbom.getSupplier(), "Author");  // todo should be author, not supplier?
        r.addContext(sbom,"author");
        results.add(r);

        // Test timestamp
        // "Record of the date and time of the SBOM data assembly"
        r = resultEmptyOrNull(sbom.getTimestamp(), "Timestamp");
        r.addContext(sbom,"timestamp");
        results.add(r);

        return results;
    }


    /**
     * Test major component fields for content
     *
     * @param c Component to test
     * @return Collection of results
     */
    private List<Result> testComponentFields(Component c){

        List<Result> results = new ArrayList<>();
        Result r;   // utility result for adding additional detail todo clunky solution, could be improved

        // Test publisher/supplier
        // "The name of an entity that creates, defines, and identifies components."
        r = resultEmptyOrNull(c.getPublisher(), "Publisher");
        r.addContext(c,"publisher");
        results.add(r);

        // Test name
        // "Designation assigned to a unit of software defined by the original supplier."
        r = resultEmptyOrNull(c.getName(), "Name");
        r.addContext(c,"name");
        results.add(r);

        // Test version
        // "Identifier used by the supplier to specify a change in software from a previously identified version."
        r = resultEmptyOrNull(c.getVersion(), "Version");
        r.addContext(c,"version");
        results.add(r);


        // Test UIDs
        // "Other identifiers that are used to identify a component, or serve as a look-up key for relevant databases."
        r = resultEmptyOrNull(c.getUniqueID(), "Unique ID");
        r.addContext(c,"uniqueID");
        results.add(r);

        r = resultEmptyOrNull(c.getCpes(), "CPEs");
        r.addContext(c,"cpes");
        results.add(r);

        r = resultEmptyOrNull(c.getPurls(), "PURLs");
        r.addContext(c,"purls");
        results.add(r);

        r = resultEmptyOrNull(c.getSwids(), "SWIDs");
        r.addContext(c,"swids");
        results.add(r);



        return results;
    }


    /**
     * Enhanced empty/null check that returns a result
     *
     * @param o Object to check
     * @return Result
     */
    private Result resultEmptyOrNull(Object o, String value){

        // Hold the pass/fail message for the result
        String message;
        // the result that will be created after the checks
        Result r;

        // Null check
        if(o == null) {
            message = String.format("%s is not present", value);
            r = new Result(TEST_NAME, Result.STATUS.FAIL,
                    message);
            r.updateInfo(Result.Context.STRING_VALUE,
                    String.format("%s not found", value));
            return r;

        }

        // Test for empty string
        if(o instanceof String){
            // Not an Empty String
            if(!o.equals("")) {
                message = String.format("%s is present", value);
                r = new Result(TEST_NAME, Result.STATUS.PASS,
                        message);
                r.updateInfo(Result.Context.STRING_VALUE, o.toString());
                return r;
            }
            // String is empty
            message = String.format("%s is not present", value);
            r = new Result(TEST_NAME, Result.STATUS.FAIL,
                    message);
            r.updateInfo(Result.Context.STRING_VALUE,
                    String.format("%s not found", value));
            return r;
        }

        // Test for list of elements (Purl, CPEs, etc)
        if(o instanceof Collection<?>){
            int size = ((Collection<?>) o).size();
            // multiple elements are present, test passes
            if(size >= 1) {
                message = String.format("%s are present", value);
                r = new Result(TEST_NAME, Result.STATUS.PASS,
                        message);
                r.updateInfo(Result.Context.STRING_VALUE, o.toString());
                return r;
            }
            // elements are not present, test fails
            message = String.format("%s are not present", value);
            r = new Result(TEST_NAME, Result.STATUS.FAIL,
                    message);
            r.updateInfo(Result.Context.STRING_VALUE,
                    String.format("%s not found", value));
            return r;
        }

        // Neither a string or integer but isn't null
        // todo shouldn't default to true
        message = String.format("%s is present", value);
        return new Result(TEST_NAME, Result.STATUS.PASS, message);
    }
}
