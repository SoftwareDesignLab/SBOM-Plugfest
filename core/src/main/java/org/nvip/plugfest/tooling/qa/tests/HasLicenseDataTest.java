package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOMType;

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
        SBOMType type = sbom.getOriginFormat();

        if(type == SBOMType.Other){
            Result r = new Result(TEST_NAME, Result.STATUS.FAIL, "Invalid SBOM " +
                    "Type");
            r.addContext(sbom, "SBOM Type");
            results.add(r);
            return results;
        }
        // for every component, test for license data
        for(Component c : sbom.getAllComponents()){
            results.addAll(testComponentLicense(c, type));
        }
        return results;
    }

    private List<Result> testComponentLicense(Component c, SBOMType type){
        List<Result> results = new ArrayList<>();
        Result r;
        // if no licenses are declared, test automatically fails
        if(isEmptyOrNull(c.getLicenses())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "No Licenses " +
                    "Detected");
            r.addContext(c, "licenses");
            results.add(r);
        }
        // licenses are present, check for data
        else{
            //TODO Do we want to check for valid SPDX license inside
            // this test?

            // spdx licenses are under PackageLicenseConcluded and
            // PackageLicenseDeclared
            if(type == SBOMType.SPDX){
                //TODO
            }
            // cyclonedx licenses are under id or name
            else{
                for(String l : c.getLicenses()){
                    // TODO check if ID or Name is not null and return
                }
            }
        }
        return results;
    }

}
