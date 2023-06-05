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
     * Test every component for cpes. If they are present, test if
     * PURLs match the component's stored data
     * @param sbom SBOM to test
     * @return a collection of results for each component and their PURL(s)
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
     * For every PURLs in the component, test that the PURLs information
     * matches the stored component data
     *
     * @param c the component to test
     * @return a list of results for each PURL in the component
     */
    private List<Result> matchingCPEs(Component c){
        List<Result> results = new ArrayList<>();
        Result r;

        // Test each stored purl
        for(String cpe: c.getCpes()){
            CPE cpeObj;
            // Try to parse PURL string
            try{
                cpeObj = new CPE(cpe);


                // Check if name is equal
//                results.add(isEqual(c, "component name", cpeObj.getName(), c.getName()));

                // If both versions are not null, test if equal
                if(cpeObj.getVersion() != null && c.getVersion() != null)
                    results.add(isEqual(c, "version", cpeObj.getVersion(), c.getVersion()));

                // If PURL is missing a version and component is not
                if(cpeObj.getVersion() != null && c.getVersion() == null){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL has version and Component does not");
                    r.addContext(c, "Purl Version");
                    r.updateInfo(Result.Context.STRING_VALUE, cpeObj.getVersion());
                    results.add(r);
                }

                // If Component is missing a version and PURL is not
                if(cpeObj.getVersion() == null && c.getVersion() != null){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component has version and PURL does not");
                    r.addContext(c, "Component Version");
                    r.updateInfo(Result.Context.STRING_VALUE, c.getVersion());
                    results.add(r);
                }

                // Check if namespace matches publisher
                // todo multiple namespaces? Are we assuming publisher is the namespace?
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
        // Check if purl value is different
        if(!cpeValue.equals(componentValue)){
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
