package org.nvip.plugfest.tooling.qa.tests;


import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.Hash;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

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
     * @param sbom SBOM to test
     * @return a collection of results from each component
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // list to hold results for each component
        List<Result> results = new ArrayList<>();

        // for every component, test for valid hashes
        for(Component c: sbom.getAllComponents()){
            results.addAll(validHash(c));
        }

        // return list of results for all components
        return results;
    }

    /**
     * Test for every hash in a given component if it is a valid schema
     * per Hash.Algorithm
     * @param c the component to test
     * @return the result of each component hash (if any are present)
     */
    private List<Result> validHash(Component c){
        List<Result> hashResults = new ArrayList<>();
        Result r;
        // get the list of hashes from the component
        List<Hash> hashList= new ArrayList<>(c.getHashes());
        // if the hash list is empty, no hashes are present, test cannot
        // be performed
        if(hashList.isEmpty()){
            r = new Result(TEST_NAME, Result.STATUS.ERROR, "Component does " +
                    "not contain hashes, test cannot be performed");
            r.addContext(c, "Hash Validation");
            hashResults.add(r);
        }
        // hashes are present for the component
        else{
            // for every hash in the component, test for its validity
            for(Hash h: hashList){
                Hash.Algorithm hashAlgo =  h.getAlgorithm();
                // hash is unknown/unsupported and not a valid type, test fails
                if(hashAlgo.equals(Hash.Algorithm.UNKNOWN)){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Hash is " +
                            "unknown or unsupported");
                }
                // hash is a valid type, test passes
                else{
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "Hash is " +
                            "a valid type");
                }
                r.addContext(c, "Hash Validation");
                hashResults.add(r);
            }

        }
        return hashResults;
    }
}
