package org.nvip.plugfest.tooling.qa.tests;


import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.Hash;
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

    private static final Set<Hash.Algorithm> validCDXAlgo = new HashSet<>(List.of(
            Hash.Algorithm.MD5,
            Hash.Algorithm.SHA1,
            Hash.Algorithm.SHA256,
            Hash.Algorithm.SHA384,
            Hash.Algorithm.SHA512,
            Hash.Algorithm.SHA3256,
            Hash.Algorithm.SHA3384,
            Hash.Algorithm.SHA3512,
            Hash.Algorithm.BLAKE2b256,
            Hash.Algorithm.BLAKE2b384,
            Hash.Algorithm.BLAKE2b512,
            Hash.Algorithm.BLAKE3
    ));

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

        // if the sbom is an SPDX sbom, all algorithms in
        // Hash.Algorithm are valid, just need to check for UNKNOWN
        if(sbom.getOriginFormat().equals(SBOM.Type.SPDX)){
            // for every component, test for valid hashes
            for(Component c: sbom.getAllComponents()){
                results.addAll(validHashSPDX(c));
            }
        }
        // else, CycloneDX sbom restricts the types of hashing algorithms
        // that can be used
        else{
            // for every component, test for valid hashes
            for(Component c: sbom.getAllComponents()){
                results.addAll(validHashCDX(c));
            }
        }

        // return list of results for all components
        return results;
    }

    /**
     * Test for every hash in a given SPDX SBOM component if it is a valid
     * schema per Hash.Algorithm
     * SPDX hashes can be the following algorithm:
     * SHA1, SHA224, SHA256, SHA384, SHA512, SHA3-256, SHA3-384, SHA3-512,
     * BLAKE2b-256, BLAKE2b-384, BLAKE2b-512, BLAKE3, MD2, MD4, MD5, MD6,
     * and ADLER32
     * @param c the component to test
     * @return the result of each component hash (if any are present)
     */
    private List<Result> validHashSPDX(Component c){
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
            r.updateInfo(Result.Context.FIELD_NAME, "Hashes");
            hashResults.add(r);
        }
        // hashes are present for the component
        else{
            // for every hash in the component, test for its validity
            for(Hash h: hashList){
                Hash.Algorithm hashAlgo =  h.getAlgorithm();
                // Hash.Algorithm holds all the valid SPDX hash algorithms,
                // and only needs to check if it is UNKNOWN
                // hash is unknown/unsupported and not a valid type, test fails
                if(hashAlgo.equals(Hash.Algorithm.UNKNOWN)){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Hash " +
                            "Algorithm is unknown or unsupported for SPDX: " +
                            hashAlgo);
                }
                // hash is a valid type, test passes
                else{
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "Hash " +
                            "Algorithm is a valid type: " + hashAlgo);
                }
                r.addContext(c, "Hash Validation");
                r.updateInfo(Result.Context.FIELD_NAME, "Hashes");
                r.updateInfo(Result.Context.STRING_VALUE, h.toString());
                hashResults.add(r);
            }

        }
        return hashResults;
    }

    /**
     * Test for every hash in a given CDX SBOM component if it is a valid
     * schema per Hash.Algorithm
     * CycloneDX hashes can be the following algorithm:
     * "MD5", "SHA-1", "SHA-256", "SHA-384", "SHA-512", "SHA3-256", "SHA3-384",
     * "SHA3-512", "BLAKE2b-256", "BLAKE2b-384", "BLAKE2b-512", and "BLAKE3"
     * @param c the component to test
     * @return the result of each component hash (if any are present)
     */
    private List<Result> validHashCDX(Component c){
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
            r.updateInfo(Result.Context.FIELD_NAME, "Hashes");
            hashResults.add(r);
        }
        // hashes are present for the component
        else{
            // for every hash in the component, test for its validity
            for(Hash h: hashList){
                Hash.Algorithm hashAlgo =  h.getAlgorithm();
                // Hash.Algorithm holds all the valid SPDX hash algorithms,
                // and only needs to check if it is UNKNOWN
                // hash is unknown/unsupported and not a valid type, test fails
                if(!validCDXAlgo.contains(hashAlgo)){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Hash " +
                            "Algorithm is unknown or unsupported for CDX: "
                            + hashAlgo);
                }
                // hash is a valid type, test passes
                else{
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "Hash " +
                            "Algorithm is a valid type for CDX: " + hashAlgo);
                }
                r.addContext(c, "Hash Validation");
                r.updateInfo(Result.Context.FIELD_NAME, "Hashes");
                r.updateInfo(Result.Context.STRING_VALUE, h.toString());
                hashResults.add(r);
            }

        }
        return hashResults;
    }
}
