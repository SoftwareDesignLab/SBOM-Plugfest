package org.nvip.plugfest.tooling.qa.tests;

import org.checkerframework.checker.units.qual.C;
import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.uids.CPE;

import java.util.ArrayList;
import java.util.List;

/**
 * file: AccurateCPETest.java
 *
 * Test purls for if they match stored component data

 * @author Derek Garcia
 */
public class AccurateCPETest extends MetricTest{
    // the test name for the results
    private static final String TEST_NAME = "AccurateCPE";

    /**
     * Test every component for CPEs. If they are present, test if
     * CPEs match the component's stored data
     * @param sbom SBOM to test
     * @return a collection of results for each component and their CPE(s)
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // list to hold results for each component
        List<Result> results = new ArrayList<>();

        // Test each component
        for(Component c: sbom.getAllComponents())
            results.addAll(matchingCPEs(c));


        return results;
    }


    /**
     * For every CPE in the component, test that the CPEs information
     * matches the stored component data
     *
     * @param c the component to test
     * @return a list of results for each CPE in the component
     */
    private List<Result> matchingCPEs(Component c){
        List<Result> results = new ArrayList<>();
        Result r;

        // Test each stored cpe
        for(String cpe: c.getCpes()){
            CPE cpeObj;
            // Try to parse CPE string
            try{
                cpeObj = new CPE(cpe);

                // test Vendor
                results.add(isEqual(c, "vendor", cpeObj.getVendor(), c.getPublisher()));

                // test name
                results.add(isEqual(c, "component name", cpeObj.getProduct(), c.getName()));

                // test version
                results.add(isEqual(c, "version", cpeObj.getVersion(), c.getVersion()));

//
//                // If PURL is missing a version and component is not
//                if(cpeObj.getVersion() != null && c.getVersion() == null){
//                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL has version and Component does not");
//                    r.addContext(c, "Purl Version");
//                    r.updateInfo(Result.Context.STRING_VALUE, cpeObj.getVersion());
//                    results.add(r);
//                }
//
//                // If Component is missing a version and PURL is not
//                if(cpeObj.getVersion() == null && c.getVersion() != null){
//                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component has version and PURL does not");
//                    r.addContext(c, "Component Version");
//                    r.updateInfo(Result.Context.STRING_VALUE, c.getVersion());
//                    results.add(r);
//                }

                // Check if namespace matches publisher
                // todo multiple namespaces? What order are are the cpe elements in?
//                if(cpeObj.getNamespace().size() == 1)
//                    results.add(isEqual(c, "publisher", cpeObj.getNamespace().get(0), c.getPublisher()));

            } catch (Exception e){
                // Failed to parse cpeObj string
                Debug.log(Debug.LOG_TYPE.DEBUG, "Unable to parse CPE \"" + cpe + "\"");
                r = new Result(TEST_NAME, Result.STATUS.FAIL, e.getMessage());

                r.updateInfo(Result.Context.FIELD_NAME, "CPE");
                r.updateInfo(Result.Context.STRING_VALUE, cpe);

                results.add(r);
            }
        }


        return results;
    }

    /**
     * Checks if 2 fields are equal
     *
     * @param c Component
     * @param field name of field that is being checked
     * @param cpeValue Value stored in the PURL string
     * @param componentValue Value stored in the Component
     * @return Result with the findings
     */
    private Result isEqual(Component c, String field, String cpeValue, String componentValue){
        Result r;
        // Check if cpe value is different
        if(CPE.isEqualWildcard(cpeValue, componentValue)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "CPE does not match " + field);
            r.updateInfo(Result.Context.STRING_VALUE, componentValue);

        // Else they both match
        } else {
            r = new Result(TEST_NAME, Result.STATUS.PASS, "CPE matches " + field);
        }

        // Add context and return
        r.addContext(c, "CPE:" + field);
        return r;
    }
}
