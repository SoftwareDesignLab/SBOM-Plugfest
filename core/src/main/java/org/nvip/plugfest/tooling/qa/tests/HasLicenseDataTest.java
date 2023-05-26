package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.Component;

import java.util.*;

/**
 * file: HasLicenseDataTest.java
 *
 * Test each component in the given SBOM if they have license data
 * @author Matthew Morrison
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
        // for every component, test for license data
        for(Component c : sbom.getAllComponents()){
            results.addAll(testComponentLicense(c));
        }
        return results;
    }

    private List<Result> testComponentLicense(Component c){
        List<Result> results = new ArrayList<>();
        Result r;
        // if no licenses are declared, test automatically fails
        if(isEmptyOrNull(c.getLicenses())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "No Licenses " +
                    "Detected");
            r.addContext(c, "licenses");
            results.add(r);
        }
        // licenses are present, check for a url,
        else{
            for(String l : c.getLicenses()){

            }
        }
        return results;
    }

}
