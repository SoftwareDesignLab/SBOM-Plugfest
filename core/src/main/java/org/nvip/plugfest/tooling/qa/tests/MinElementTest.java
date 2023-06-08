package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

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

        return results;
    }


    /**
     * Enhanced empty/null check that returns a result
     *
     * @param o Object to check
     * @return Result
     */
    private Result resultEmptyOrNull(Object o, String value){

        String message;

        // Null check
        if(o == null) {
            message = String.format("%s is not present in the SBOM", value);
            return new Result(TEST_NAME, Result.STATUS.FAIL, message);

        }

        // Test for empty string
        if(o instanceof String){
            // Not an Empty String
            if(!o.equals("")) {
                message = String.format("%s is present in the SBOM", value);
                Result r = new Result(TEST_NAME, Result.STATUS.PASS,
                        message);
                r.updateInfo(Result.Context.STRING_VALUE, o.toString());
                return r;
            }
            // String is empty
            message = String.format("%s is not present in the SBOM", value);
            return new Result(TEST_NAME, Result.STATUS.FAIL,
                    message);
        }
        // Not a string but isn't null
        // todo shouldn't default to true
        message = String.format("%s is present in the SBOM", value);
        return new Result(TEST_NAME, Result.STATUS.PASS, message);
    }
}
