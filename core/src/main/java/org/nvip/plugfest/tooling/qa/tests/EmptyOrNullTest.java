package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.Signature;

import java.util.*;

/**
 * file: EmptyOrNullTest.java
 *
 * Test each field in the given SBOM to find fields that are empty or null
 * @author Derek Garcia
 */
public class EmptyOrNullTest extends MetricTest {
    private static final String TEST_NAME = "EmptyOrNull";

    /**
     * Test SBOM fields for null or empty values
     *
     * @param sbom SBOM to test
     * @return Collection of results
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
     * Test major SBOM fields for content
     * @param sbom SBOM to test
     * @return Collection of results
     */
    private List<Result> testSBOMFields(SBOM sbom){
        List<Result> results = new ArrayList<>();
        Result r;   // utility result for adding additional detail todo clunky solution, could be improved

        // Test Spec version
        r = resultEmptyOrNull(sbom.getSpecVersion());
        r.addContext(sbom,"specVersion");
        results.add(r);

        // Test serialNumber
        r = resultEmptyOrNull(sbom.getSerialNumber());
        r.addContext(sbom,"serialNumber");
        results.add(r);

        // Test supplier
        r = resultEmptyOrNull(sbom.getSupplier());
        r.addContext(sbom,"supplier");
        results.add(r);

        // Test timestamp
        r = resultEmptyOrNull(sbom.getTimestamp());
        r.addContext(sbom,"timestamp");
        results.add(r);

        if(isEmptyOrNull(sbom.getSignature())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is missing");
            r.addContext(sbom,"signatures");
            results.add(r);
        } else {
            for(Signature s : sbom.getSignature()){
                r = resultEmptyOrNull(s);
                r.addContext(sbom,"signature");
                results.add(r);
            }
        }

        return results;
    }


    /**
     * Test major component fields for content
     * @param c Component to test
     * @return Collection of results
     */
    private List<Result> testComponentFields(Component c){

        List<Result> results = new ArrayList<>();
        Result r;   // utility result for adding additional detail todo clunky solution, could be improved

        // Test name
        r = resultEmptyOrNull(c.getName());
        r.addContext(c,"name");
        results.add(r);

        // Test publisher
        r = resultEmptyOrNull(c.getPublisher());
        r.addContext(c,"publisher");
        results.add(r);

        // Test version
        r = resultEmptyOrNull(c.getVersion());
        r.addContext(c,"version");
        results.add(r);

        // Test Licences
        if(isEmptyOrNull(c.getLicenses())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Missing");
            r.addContext(c,"licenses");
            results.add(r);
        } else {
            // Check each license
            for(String l : c.getLicenses()){
                r = resultEmptyOrNull(l);
                r.addContext(c,"license");
                results.add(r);
            }
        }

        // Test CPEs
        if(isEmptyOrNull(c.getCpes())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Missing");
            r.addContext(c,"cpes");
            results.add(r);
        } else {
            // Check each CPE
            for(String l : c.getCpes()){
                r = resultEmptyOrNull(l);
                r.addContext(c,"cpe");
                results.add(r);
            }
        }

        // Test Purls
        if(isEmptyOrNull(c.getPurls())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Missing");
            r.addContext(c,"purls");
            results.add(r);
        } else {
            // Check each purl
            for(PURL p : c.getPurls()){
                r = resultEmptyOrNull(p);
                r.addContext(c,"purl");
                results.add(r);
            }
        }

        // Test SWIDs
        if(isEmptyOrNull(c.getSwids())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Value is Missing");
            r.addContext(c,"swids");
            results.add(r);
        } else {
            // check each swid
            for(String s : c.getSwids()){
                r = resultEmptyOrNull(s);
                r.addContext(c,"swid");
                results.add(r);
            }
        }

        // Test UID
        r = resultEmptyOrNull(c.getUniqueID());
        r.addContext(c,"uniqueID");
        results.add(r);

        return results;
    }


    /**
     * Enhanced empty/null check that returns a result
     *
     * @param o Object to check
     * @return Result
     */
    private Result resultEmptyOrNull(Object o){
        // Null check
        if(o == null)
            return new Result(TEST_NAME, Result.STATUS.FAIL, "Value is null");

        // Test for empty string
        if(o instanceof String){
            // Not an Empty String
            if(!o.equals("")) {
                Result r = new Result(TEST_NAME, Result.STATUS.PASS, "Value is not an empty string");
                r.updateInfo(Result.Context.STRING_VALUE, o.toString());
                return r;
            }
            // String is empty
            return new Result(TEST_NAME, Result.STATUS.FAIL, "Value is an empty string");
        }
        // Not a string but isn't null
        // todo shouldn't default to true
        return new Result(TEST_NAME, Result.STATUS.PASS, "Value is not null");
    }
}
