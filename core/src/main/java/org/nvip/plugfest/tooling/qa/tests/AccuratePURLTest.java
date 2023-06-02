package org.nvip.plugfest.tooling.qa.tests;

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
     * Test ecery component for purls. If they are present, test if
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
     * @param c the component to test
     * @return a list of results for each PURL in the component
     */
    private List<Result> matchingPURLs(Component c){
        List<Result> purlResults = new ArrayList<>();
        Result r;
        Set<PURL> componentPURL = c.getPurls();
        // if no purls are present in the component, test cannot be performed
        if(componentPURL.isEmpty()){
            r = new Result(TEST_NAME, Result.STATUS.ERROR, "Component does " +
                    "not contain PURLs");
            r.addContext(c, "Matching PURLs");
            r.updateInfo(Result.Context.FIELD_NAME, "PURL");
            purlResults.add(r);
        }
        // purls are present in the component, test each purl for matching
        // info with component
        else{
            for(PURL p: componentPURL){
                // test the validity of the purl's name and add its result
                purlResults.add(testPURLName(c, p));
                // test the validity of the purl's version and add its result
                purlResults.add(testPURLVersion(c, p));

                //TODO Component's need to hold group/vendor info in order
                // to compare with PURL
            }

        }
        return purlResults;

    }

    /**
     * Method checks if the purl's name matches the components name
     * @param c the component to compare with
     * @param p the purl to compare with
     * @return the result of checking if the PURL and component name matches
     */
    private Result testPURLName(Component c, PURL p){
        // get the purl and component's name
        String purlName = p.getName();
        String componentName = c.getName();
        Result r;

        // if purlName is empty, test automatically fails, cannot check
        // if names match
        if(isEmptyOrNull(purlName)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL " +
                    "does not have a name");
        }
        // if the two names do not match, the test fails
        else if(!purlName.equals(componentName)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL " +
                    "name does not match component's name");
        }
        // if the two names do match, the test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL " +
                    "name matches component's name");
        }
        // add context about the findings
        r.addContext(c, "Matching PURL data");
        r.updateInfo(Result.Context.FIELD_NAME, "PURL Name");
        r.updateInfo(Result.Context.STRING_VALUE, p.toString());

        return r;
    }

    /**
     * Method checks if the purl's version matches the components version
     * @param c the component to compare with
     * @param p the purl to compare with
     * @return the result of checking if the PURL and component version matches
     */
    private Result testPURLVersion(Component c, PURL p){
        // get the purl and component's version
        String purlVersion = p.getVersion();
        String componentVersion = c.getVersion();

        Result r;
        // if purlVersion is empty, test automatically fails, cannot check
        // if versions match
        if(isEmptyOrNull(purlVersion)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL " +
                    "does not have a version");
            r.addContext(c, "Matching PURL data");
            r.updateInfo(Result.Context.FIELD_NAME, "PURL version");
            // if purl is null, toString will produce NPE, so it needs to
            // be skipped
            return r;
        }
        // since version is not a required element.
        // if the component does not have a version, test cannot be performed
        else if(isEmptyOrNull(componentVersion)){
            r = new Result(TEST_NAME, Result.STATUS.ERROR, "Component does " +
                    "not have version, test cannot be completed");
        }
        // if the two versions do not match, the test fails
        else if(!purlVersion.equals(componentVersion)){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL " +
                    "version does not match component's version");
        }
        // if the two versions do match, the test passes
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "PURL " +
                    "version matches component's version");
        }
        // add context about the findings
        r.addContext(c, "Matching PURL data");
        r.updateInfo(Result.Context.FIELD_NAME, "PURL version");
        r.updateInfo(Result.Context.STRING_VALUE, p.toString());

        return r;
    }
}
