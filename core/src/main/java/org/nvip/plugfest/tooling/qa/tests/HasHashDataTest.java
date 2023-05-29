package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

public class HasHashDataTest extends MetricTest{

    /**
     * Test all SBOM components for hashes
     * @param sbom SBOM to test
     * @return a collection of results if each component contains
     * hashes and how many
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();

        for(Component c: sbom.getAllComponents()){
            results.add(hasHashData(c));
        }

        return results;
    }

    /**
     * Test to determine how many hashes are in a given component
     * @param c the component to test
     * @return the result of the component's hashes (if any are found)
     */
    public Result hasHashData(Component c){
        Result r;
        int hashes;

        return null;

    }
}
