package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

/**
 * file: HasMinElsMetaDataTest.java
 *
 * Test the SBOM's document creation info and metadata to ensure all minimum
 * required elements are in the SBOM
 * @author Matthew Morrison
 */
public class HasMinElsMetaDataTest extends MetricTest{

    private static final String TEST_NAME = "HasMinElsMetaData";
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>(testSBOMFields(sbom));

        return results;
    }

    /**
     * Test the minimum elements required for an SBOM's info and metadata
     * and if they are included in the SBOM
     * @param sbom the sbom to test
     * @return a collection of results for each min element in the SBOM's info
     * and metadata
     */
    private List<Result> testSBOMFields(SBOM sbom){
        List<Result> results = new ArrayList<>();
        Result r;   // utility result for adding additional detail todo clunky solution, could be improved

        // Test Spec version and add its result
        results.add(hasSpecVersion(sbom));

        //Test for SBOM version and add its result
        results.add(hasSBOMVersion(sbom));

        // Test for SBOM type and add its result
        results.add(hasSBOMType(sbom));

        // Test serialNumber and add its result
        results.add(hasSerialNumber(sbom));

        // Test supplier and add its result
        results.add(hasSupplier(sbom));

        // Test timestamp and add its result
        results.add(hasTimeStamp(sbom));

        //TODO SPDX Requires the following:
        // DataLicense, SPDXID, DocumentName, DocumentNamespace
        // when these are implemented, add a check for these elements


        return results;
    }

    /**
     * Test if the SBOM has a spec version
     * @param sbom the sbom to test
     * @return the result of if the SBOM has a spec version
     */
    private Result hasSpecVersion(SBOM sbom){
        Result r;
        // if the sbom's spec version is null, then the sbom did not
        // include it, test fails
        if(isEmptyOrNull(sbom.getSpecVersion())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "SBOM does " +
                    "not contain spec version");
        }
        // sbom included the spec version, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "SBOM contains " +
                    "spec version");
            r.updateInfo(Result.Context.STRING_VALUE, sbom.getSpecVersion());
        }
        // adding context for the test
        r.addContext(sbom,"specVersion");
        r.updateInfo(Result.Context.FIELD_NAME, "specVersion");
        return r;
    }

    /**
     * Test if the SBOM has its version included
     * @param sbom the sbom to test
     * @return the result of if the SBOM has its version
     */
    private Result hasSBOMVersion(SBOM sbom){
        Result r;
        // if the sbom's version is null, then the sbom did not
        // include it, test fails
        if(isEmptyOrNull(sbom.getSbomVersion())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "SBOM does " +
                    "not contain SBOM version");
        }
        // sbom included its version, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "SBOM contains " +
                    "SBOM version");
            r.updateInfo(Result.Context.STRING_VALUE, sbom.getSbomVersion());
        }
        // adding context for result
        r.addContext(sbom,"sbomVersion");
        r.updateInfo(Result.Context.FIELD_NAME, "sbomVersion");
        return r;
    }

    /**
     * Test to check if the SBOM included its type (CDX,SPDX)
     * @param sbom the sbom to test
     * @return a result of if the SBOM included its type
     */
    private Result hasSBOMType(SBOM sbom){
        Result r;
        // if the sbom's type is null, then the sbom did not
        // include it, test fails
        if(isEmptyOrNull(sbom.getOriginFormat())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "SBOM does " +
                    "not include its type");
        }
        // sbom included its type, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "SBOM included " +
                    "its type");
            // add context based on what type of SBOM was passed through
            if(sbom.getOriginFormat().equals(SBOM.Type.CYCLONE_DX)){
                r.updateInfo(Result.Context.FIELD_NAME, "bomFormat");
            } else{
                r.updateInfo(Result.Context.FIELD_NAME, "SPDXVersion");
            }
            r.updateInfo(Result.Context.STRING_VALUE,
                    sbom.getOriginFormat().toString());
        }
        // adding context for result
        r.addContext(sbom,"SBOM Type");
        return r;
    }

    /**
     * Test if the SBOM included its supplier
     * @param sbom the sbom to test
     * @return a result of if the sbom included its supplier
     */
    private Result hasSupplier(SBOM sbom){
        Result r;
        // if the sbom's supplier is null, then the sbom did not
        // include it, test fails
        if(isEmptyOrNull(sbom.getSupplier())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "SBOM does " +
                    "not contain its supplier");
        }
        // sbom included its supplier, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "SBOM contains " +
                    "its supplier");
            r.updateInfo(Result.Context.STRING_VALUE, sbom.getSupplier());
        }
        // adding context for result
        r.addContext(sbom,"Supplier");
        r.updateInfo(Result.Context.FIELD_NAME, "Supplier/Creators/Authors");
        return r;
    }


    /**
     * Test if the SBOM included a serial number
     * @param sbom the sbom to test
     * @return a result of if the sbom included a serial number
     */
    private Result hasSerialNumber(SBOM sbom){
        Result r;
        // if the sbom's serial number is null, then the sbom did not
        // include it, test fails
        if(isEmptyOrNull(sbom.getSerialNumber())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "SBOM does " +
                    "not contain a serial number");
        }
        // sbom included a serial number, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "SBOM contains " +
                    "a serial number");
            r.updateInfo(Result.Context.STRING_VALUE, sbom.getSerialNumber());
        }
        // adding context for result
        r.addContext(sbom,"Serial Number");
        r.updateInfo(Result.Context.FIELD_NAME, "serialNumber");
        return r;
    }

    /**
     * Test if the SBOM included a time stamp of creation
     * @param sbom the sbom to test
     * @return a result of if the sbom included a time stamp
     */
    private Result hasTimeStamp(SBOM sbom){
        Result r;
        // if the sbom's time stamp is null, then the sbom did not
        // include it, test fails
        if(isEmptyOrNull(sbom.getTimestamp())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "SBOM does " +
                    "not contain its time stamp");
        }
        // sbom included a time stamp, test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "SBOM contains " +
                    "its time stamp");
            r.updateInfo(Result.Context.STRING_VALUE, sbom.getTimestamp());
        }
        // adding context for result
        r.addContext(sbom,"Time Stamp");
        r.updateInfo(Result.Context.FIELD_NAME, "timestamp");
        return r;
    }
}
