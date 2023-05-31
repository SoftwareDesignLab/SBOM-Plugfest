package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

/**
 * file: HasBomRefTest.java
 *
 * For every component in a given CDX SBOM, test if the component has a
 * unique identifier to reference inside the BOM (bom-ref for CDX)
 * @author Matthew Morrison
 */
public class HasBomRefTest extends MetricTest{
    private static final String TEST_NAME = "HasBomRef";

    /**
     * Test every component for a bom-ref
     * @param sbom SBOM to test
     * @return a collection of results from each component in the SBOM
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();

        // loop through each component and test
        for(Component c: sbom.getAllComponents()){
            results.add(checkBomRef(c));
        }
        return results;
    }

    /**
     * For a given component, chec if a unique id (bom-ref for cdx) is
     * present
     * @param c the component to test
     * @return the result of the component and if a bom-ref is present
     */
    private Result checkBomRef(Component c){
        Result r;
        // if unique id is empty/null, then there is no bom-ref
        if(isEmptyOrNull(c.getUniqueID())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component " +
                    "does not have bom-ref identifier");
        }
        // a bom-ref is present
        else{
            r = new Result(TEST_NAME, Result.STATUS.PASS, "Component " +
                    "has bom-ref identifier");
        }
        r.addContext(c,"Bom-Ref Presence");
        return r;
    }
}
