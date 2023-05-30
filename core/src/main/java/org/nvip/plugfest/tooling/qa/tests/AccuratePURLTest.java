package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.PURL;
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

    private static final String TEST_NAME = "AccuratePURL";
    @Override
    public List<Result> test(SBOM sbom) {
        // list to hold results for each component
        List<Result> results = new ArrayList<>();

        for(Component c: sbom.getAllComponents()){

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
            purlResults.add(r);
            return purlResults;
        }
        // purls are present in the component, test each purl for matching
        // info with component
        else{
            // hold the component name and version
            String componentName = c.getName();
            String componentVersion = c.getVersion();
            for(PURL p: componentPURL){
                String name = p.getName();
                String version = p.getVersion();

                if(name.equals(componentName) &&
                        version.equals(componentVersion)){
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "PURL " +
                            "matches component data");
                }
                else{
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "PURL " +
                            " does not match component data");
                }
                r.addContext(p, "Matching PURLs");
                purlResults.add(r);

            }
        }



        return null;
    }
}
