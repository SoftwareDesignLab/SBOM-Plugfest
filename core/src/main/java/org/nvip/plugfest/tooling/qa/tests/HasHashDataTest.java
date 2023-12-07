/ **
* Copyright 2021 Rochester Institute of Technology (RIT). Developed with
* government support under contract 70RCSA22C00000008 awarded by the United
* States Department of Homeland Security for Cybersecurity and Infrastructure Security Agency.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the “Software”), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* /

package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.sbom.Component;

import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.uids.Hash;

import java.util.ArrayList;
import java.util.List;

/**
 * file: HasHashDataTest.java
 *
 * Test each component if hash values are present
 * @author Matthew Morrison
 */
public class HasHashDataTest extends MetricTest{

    private static final String TEST_NAME = "HasHashData";
    /**
     * Test all SBOM components for hashes
     * @param sbom SBOM to test
     * @return a collection of results if each component contains
     * hashes and how many
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // list to hold results for each component
        List<Result> results = new ArrayList<>();

        // for every component in the sbom, check for hash values
        for(Component c: sbom.getAllComponents()){
            results.add(hasHashData(c));
        }
        // return list of results for all components
        return results;
    }

    /**
     * Test to determine how many hashes are in a given component
     * @param c the component to test
     * @return the result of the component's hashes (if any are found)
     */
    public Result hasHashData(Component c){
        Result r;
        // get the list of hashes from the component
        List<Hash> hashList = new ArrayList<>(c.getHashes());
        // if the hash list is empty, no hashes are present, test fails
        if(hashList.isEmpty()){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component does " +
                    "not contain hashes");
            r.updateInfo(Result.Context.STRING_VALUE, "No Hashes Present");
        }
        // hashes are present for the component, count how many hashes
        // the component has
        else{
            ArrayList<String> hashAlgos = new ArrayList<>();
            for(Hash h : hashList){
                hashAlgos.add(h.getAlgorithm().toString());
            }
            String message = String.format("Component contains %d hashes",
                    hashList.size());
            r = new Result(TEST_NAME, Result.STATUS.PASS, message);
            r.updateInfo(Result.Context.STRING_VALUE, "Hashes Present: " +
                    String.join(", ", hashAlgos));
        }
        r.updateInfo(Result.Context.TYPE, "Component");
        r.updateInfo(Result.Context.IDENTIFIER, c.getName());
        r.updateInfo(Result.Context.FIELD_NAME, "hashes");
        return r;

    }
}
