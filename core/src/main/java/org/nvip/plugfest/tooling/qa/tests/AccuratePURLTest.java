package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.uids.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * file: AccuratePURLTest.java
 *
 * Test purls for if they match stored component data
 * @author Matthew Morrison
 */
public class AccuratePURLTest extends MetricTest{
    // the test name for the results
    private static final String TEST_NAME = "AccuratePURL";

    /**
     * Test every component for purls. If they are present, test if
     * PURLs match the component's stored data
     * @param sbom SBOM to test
     * @return a collection of results for each component and their PURL(s)
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // list to hold results for each component
        List<Result> results = new ArrayList<>();

        for(Component c: sbom.getAllComponents()){
            results.addAll(matchingPURLs(c));
        }

        return results;
    }

    /**
     * For every PURLs in the component, test that the PURLs information
     * matches the stored component data
     *
     * @param c the component to test
     * @return a list of results for each PURL in the component
     */
    private List<Result> matchingPURLs(Component c){
        List<Result> purlResults = new ArrayList<>();
        Result r;
        /*
        // Test each stored purl
        for(String p: c.getPurls()){
            PURL purl;
            // Try to parse PURL string
            try{
                purl = new PURL(p);

                // Check if name is equal
                purlResults.add(isEqual(c, "component name", purl.getName(), c.getName()));

                // If both versions are not null, test if equal
                if(purl.getVersion() != null && c.getVersion() != null)
                    purlResults.add(isEqual(c, "version", purl.getVersion(), c.getVersion()));

                // If PURL is missing a version and component is not
                if(purl.getVersion() != null && c.getVersion() == null){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL has version and Component does not");
                    r.addContext(c, "Purl Version");
                    r.updateInfo(Result.Context.STRING_VALUE, purl.getVersion());
                    purlResults.add(r);
                }

                // If Component is missing a version and PURL is not
                if(purl.getVersion() == null && c.getVersion() != null){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component has version and PURL does not");
                    r.addContext(c, "Component Version");
                    r.updateInfo(Result.Context.STRING_VALUE, c.getVersion());
                    purlResults.add(r);
                }

                // Check if namespace matches publisher
                // todo multiple namespaces? Are we assuming publisher is the namespace?
                if(purl.getNamespace().size() == 1)
                    purlResults.add(isEqual(c, "publisher", purl.getNamespace().get(0), c.getPublisher()));

            } catch (Exception e){
                // Failed to parse purl string
                Debug.log(Debug.LOG_TYPE.DEBUG, "Unable to parse PURL \"" + p + "\"");
                r = new Result(TEST_NAME, Result.STATUS.FAIL, e.getMessage());

                r.updateInfo(Result.Context.FIELD_NAME, "PURL");
                r.updateInfo(Result.Context.STRING_VALUE, p);

                purlResults.add(r);
            }
        }
         */

        return purlResults;
    }

    /**
     * Checks if 2 fields are equal
     *
     * @param c Component
     * @param field name of field that is being checked
     * @param purlValue Value stored in the PURL string
     * @param componentValue Value stored in the Component
     * @return Result with the findings
     */
    private Result isEqual(Component c, String field, String purlValue, String componentValue){
        Result r;
        // Check if purl value is different
        if(!purlValue.equals(componentValue)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL does not match " + field);
            r.updateInfo(Result.Context.STRING_VALUE, componentValue);

            // Else they both match
        } else {
            r = new Result(TEST_NAME, Result.STATUS.PASS, "PURL matches " + field);
        }

        // Add context and return
        r.addContext(c, "PURL:" + field);
        return r;
    }
}
