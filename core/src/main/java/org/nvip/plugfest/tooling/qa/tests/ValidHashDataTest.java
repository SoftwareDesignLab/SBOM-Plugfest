package org.nvip.plugfest.tooling.qa.tests;


import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.uids.Hash;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * file: ValidHashDataTest.java
 *
 * Test each component's hashes if they match schema
 * @author Matthew Morrison
 */
public class ValidHashDataTest extends MetricTest{

    private static final String TEST_NAME = "ValidHashData";

    /**
     * Test all component's hashes if they are a valid schema (if present)
     *
     * @param sbom SBOM to test
     * @return a collection of results from each component
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // list to hold results for each component
        List<Result> results = new ArrayList<>();
        Result r;
        for(Component c: sbom.getAllComponents()){

            // Skip if no hashes
            if(c.getHashes().isEmpty())
                continue;

            // Check all stored hashes
            for(Hash hash : c.getHashes()){

                // Check for unsupported hash
                if(sbom.getOriginFormat() == SBOM.Type.CYCLONE_DX && Hash.isSPDXExclusive(hash.getAlgorithm())){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "CycloneDX SBOM has an unsupported Hash");
                    r.addContext(c, "Hash");
                    r.updateInfo(Result.Context.STRING_VALUE, hash.getValue());
                    results.add(r);
                }



            }


        }

        // return list of results for all components
        return results;
    }
}
