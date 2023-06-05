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

    // SPDX  SHA224,    ,   BLAKE2b-512, MD2, MD4, MD6, ADLER32
    // CDX
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

        for(Component c: sbom.getAllComponents()){

            if(c.getHashes().isEmpty())
                continue;

            for(Hash hash : c.getHashes()){
                if(sbom.getOriginFormat() == SBOM.Type.CYCLONE_DX && Hash.SPDXAlgorithm..getAlgorithm())
            }


        }

        // return list of results for all components
        return results;
    }
}
