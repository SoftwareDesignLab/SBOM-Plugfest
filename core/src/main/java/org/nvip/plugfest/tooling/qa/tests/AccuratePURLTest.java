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
import org.nvip.plugfest.tooling.sbom.uids.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * file: AccuratePURLTest.java
 *
 * Test purls for if they match stored component data
 * @author Matthew Morrison
 * @author Derek Garcia
 */
public class AccuratePURLTest extends MetricTest{
    // the test name for the results
    private static final String TEST_NAME = "AccuratePURL";

    /**
     * Test every component for purls. If they are present, test if
     * PURLs match the component's stored data
     * @param sbom SBOM to test
     * @return a collection of results for each component and their PURL(s)
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // list to hold results for each component
        List<Result> results = new ArrayList<>();

        for(Component c: sbom.getAllComponents()){
            results.addAll(matchingPURLs(c));
        }

        return results;
    }

    /**
     * For every PURLs in the component, test that the PURLs information
     * matches the stored component data
     *
     * @param c the component to test
     * @return a list of results for each PURL in the component
     */
    private List<Result> matchingPURLs(Component c){
        List<Result> purlResults = new ArrayList<>();
        Result r;
        // Test each stored purl
        for(String p: c.getPurls()){
            PURL purl;
            // Try to parse PURL string
            try{
                purl = new PURL(p);

                // Check if name is equal
                // both at minimum should have a name
                purlResults.add(isEqual(c, "component name", purl.getName(), c.getName()));

                // test version

                // Test if purl and/or component is missing a version
                r = hasNullValues(c, purl.getVersion(), c.getVersion(), "Version");

                // both component and purl have versions, continue to comparison test
                if(r == null){
                    purlResults.add(isEqual(c, "version", purl.getVersion(), c.getVersion()));
                }
                // CPE and/or Component is missing a version, add result to list
                else{
                    purlResults.add(r);
                }

                // Check namespace
                //TODO determine how namespace is used? Per package manager?


            } catch (Exception e){
                // Failed to parse purl string
                Debug.log(Debug.LOG_TYPE.DEBUG, "Unable to parse PURL \"" + p + "\"");
                r = new Result(TEST_NAME, Result.STATUS.FAIL, e.getMessage());

                r.updateInfo(Result.Context.FIELD_NAME, "PURL");
                r.updateInfo(Result.Context.STRING_VALUE, p);

                purlResults.add(r);
            }
        }


        return purlResults;
    }

    /**
     * Checks if 2 fields are equal
     *
     * @param c Component
     * @param field name of field that is being checked
     * @param purlValue Value stored in the PURL string
     * @param componentValue Value stored in the Component
     * @return Result with the findings
     */
    private Result isEqual(Component c, String field, String purlValue, String componentValue){
        Result r;
        // Check if purl value is different
        if(!purlValue.equals(componentValue)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL does not match " + field);
            String errorMessage = String.format("Expected: %s. " +
                    "Actual: %s", componentValue, purlValue);
            r.updateInfo(Result.Context.STRING_VALUE, errorMessage);

            // Else they both match
        } else {
            r = new Result(TEST_NAME, Result.STATUS.PASS, "PURL matches " + field);
        }

        // Add context and return
        r.addContext(c, "PURL: " + field);
        r.updateInfo(Result.Context.FIELD_NAME, field);
        return r;
    }

    /**
     * For testing in optional fields, test if a field is present for both
     * the purl and component
     * @param purlValue value stored in the purl string
     * @param componentValue value stored in the Component
     * @return a result if one or both values are null OR null if both values
     * are present and not empty/null
     */
    private Result hasNullValues(Component c, String purlValue, String componentValue, String fieldName){
        // booleans to hold if purl and/or component field are present or not
        // (true if empty/null, else false)
        boolean purlValueNull = isEmptyOrNull(purlValue);
        boolean componentValueNull = isEmptyOrNull(componentValue);

        // at least one of the fields is null
        Result r;
        String failMessage;
        String contextMessage = String.format("PURL %s", fieldName);
        // If component is missing the field info and CPE is not
        if(!purlValueNull && componentValueNull){
            failMessage = String.format("PURL has %s info and component does " +
                    "not", fieldName);
            r = new Result(TEST_NAME, Result.STATUS.FAIL, failMessage);
            r.addContext(c, contextMessage);
            r.updateInfo(Result.Context.STRING_VALUE, purlValue);
            return r;
        }
        // If CPE is missing the field info and component is not
        else if(purlValueNull && !componentValueNull){
            failMessage = String.format("Component has %s info and PURL does " +
                    "not", fieldName);
            r = new Result(TEST_NAME, Result.STATUS.FAIL, failMessage);
            r.addContext(c, contextMessage);
            return r;
        }
        // If both component and CPE are missing the field's info
        else if(purlValueNull && componentValueNull){
            failMessage = String.format("Both Component and PURL missing %s " +
                    "info", fieldName);
            r = new Result(TEST_NAME, Result.STATUS.FAIL, failMessage);
            r.addContext(c, contextMessage);
            return r;
        }
        // both fields are not null and have values, return null so actual
        // test for comparison can occur
        else{
            return null;
        }
    }
}
