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
        // if SBOM is a valid type, test licenses based on its type
        else if(type == SBOMType.CYCLONE_DX){
            for(Component c : sbom.getAllComponents()){
                results.addAll(testComponentLicenseCDX(c));
            }
        } else{
            for(Component c : sbom.getAllComponents()){
                results.addAll(testComponentLicenseSPDX(c));
            }
        }


        return results;
    }

    /**
     * for every component in a CycloneDX SBOM, test if there is valid data present
     * for the component's licenses
     * @param c the component to be tested
     * @return a collection of results of every license for the component
     */
    private List<Result> testComponentLicenseCDX(Component c){
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
            // TODO Where can I get detailed info for the licenses?
            // cyclonedx licenses are under id or name
                for(String l : c.getLicenses()){
                    // TODO check if ID or Name is not null and return
                }

        }
        return results;
    }

    /**
     * for every component in an SPDX SBOM, test if there is valid data present
     * for the component's licenses
     * @param c the component to be tested
     * @return a collection of results of every license for the component
     */
    private List<Result> testComponentLicenseSPDX(Component c){
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
            // TODO Where can I get detailed info for the licenses?

            // spdx licenses are under PackageLicenseConcluded and
            // PackageLicenseDeclared
                //TODO check for PackageLicenseConcluded or
                // PackageLicenseDeclared

        }
        return results;
    }


}
