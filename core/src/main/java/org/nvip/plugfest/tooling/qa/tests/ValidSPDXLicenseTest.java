package org.nvip.plugfest.tooling.qa.tests;


import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.*;

/**
 * file: ValidSPDXLicenseTest.java
 *
 * Test each component in the given SBOM if the license is an existing
 * valid SPDX License
 * @author Matthew Morrison
 */
public class ValidSPDXLicenseTest extends MetricTest{

    private static final String TEST_NAME = "ValidSPDXLicense";

    /**
     * Test all SBOM components for their licenses and if they are a valid SPDX
     * License
     * (url, id, name, etc)
     * @param sbom SBOM to test
     * @return Collection of results for each component
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // create a list to hold each result of sbom components
        List<Result> results = new ArrayList<>();
        // for every component, test for valid SPDX Licenses
        for(Component c : sbom.getAllComponents()){
            results.addAll(testSPDXLicense(c));
        }
        return results;
    }

    private List<Result> testSPDXLicense(Component c){
        List<Result> results = new ArrayList<>();
        Result r;
        // if no licenses are declared, test automatically fails
        if(isEmptyOrNull(c.getLicenses())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "No Licenses " +
                    "Detected");
            r.addContext(c, "licenses");
            results.add(r);
        }
        return results;
    }
}
