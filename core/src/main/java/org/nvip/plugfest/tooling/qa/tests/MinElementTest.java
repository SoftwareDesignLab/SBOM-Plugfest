package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

public class MinElementTest extends MetricTest {
    private static final String TEST_NAME = "EmptyOrNull";
    /**
     * Tests minimum element
     * @param sbom SBOM to test
     * @return
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
     * Testing the data fields
     * @param sbom
     * @return
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
