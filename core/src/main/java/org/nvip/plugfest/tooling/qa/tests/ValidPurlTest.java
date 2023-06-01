package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

/**
 * file: ValidPurlTest.java
 *
 * Tests if the purls are valid
 *
 * @author Derek Garcia
 */
public class ValidPurlTest extends MetricTest {
    private static final String TEST_NAME = "ValidPurl";

    /**
     * Validates the PURL
     *
     * @param sbom SBOM to test
     * @return Collection of results
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();

        /*
        // TODO once PR changes have been added. Purls will be Strings
        for (Component c : sbom.getAllComponents()) {
            // Skip if no PURLs
            if(isEmptyOrNull(c.getPurls()))
                continue;
            Result r;
            // Else attempt to make object
            for (String p : c.getPurls()) {
                // Attempt to make new Purl Object
                try{
                    new PURL(p);    // throws error if given purl string is invalid
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "Valid Purl String");
                } catch (Exception e){
                    Debug.log(Debug.LOG_TYPE.WARN, "Failed to parse PURL \"" + p +"\" | " + e.getMessage());    // log incase regex fails
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Invalid Purl String");
                }

                r.addContext(c, "purl");
                results.add(r);
            }
        }
         */

        // return findings
        return results;
    }
}