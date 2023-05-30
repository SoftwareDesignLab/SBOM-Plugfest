package org.nvip.plugfest.tooling.qa.tests;


import org.cyclonedx.model.License;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * file: ValidSPDXLicenseTest.java
 *
 * Test each component in the given SBOM if the license is an existing
 * valid SPDX License
 * @author Matthew Morrison
 */
public class ValidSPDXLicenseTest extends MetricTest{

    private static final String TEST_NAME = "ValidSPDXLicense";

    /**
     * Test all SBOM components for their licenses and if they are a valid SPDX
     * License
     * (url, id, name, etc)
     * @param sbom SBOM to test
     * @return Collection of results for each component
     */
    @Override
    public List<Result> test(SBOM sbom) {
        // create a list to hold each result of sbom components
        List<Result> results = new ArrayList<>();
        // for every component, test for valid SPDX Licenses
        for(Component c : sbom.getAllComponents()){
            results.addAll(testSPDXLicense(c));
        }
        return results;
    }

    /**
     * Test a component's licenses to see if it is a valid SPDX license
     * via url
     * @param c the component to be tested
     * @return a collection of results for each license of a component and
     * its validity
     */
    private List<Result> testSPDXLicense(Component c) {
        List<Result> results = new ArrayList<>();
        Result r;
        // if no licenses are declared, test automatically fails
        if(isEmptyOrNull(c.getLicenses())){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "No Licenses " +
                    "Detected");
            r.addContext(c, "licenses");
            results.add(r);
        }
        else{
            StringBuilder link = new StringBuilder();
            URL url;
            // for every license id
            //TODO only held as a string, if ID is not the identifier, this fails
            // Can also check if the license provides a url as well
            for(String l : c.getLicenses()){
                try {
                    // build the link with the given license
                    link.append("https://spdx.org/licenses/").append(l)
                            .append(".html");
                    // set this link as a new URL
                    url = new URL(link.toString());
                    // set up a connection
                    HttpURLConnection huc =
                            (HttpURLConnection) url.openConnection();
                    huc.setRequestMethod("GET");
                    // get the response code from this url
                    int responseCode = huc.getResponseCode();
                    // if the response code is 200 (HTTP is okay), it is a
                    // valid SPDX License
                    if(responseCode == HttpURLConnection.HTTP_OK){
                        r = new Result(TEST_NAME, Result.STATUS.PASS, "Valid " +
                                "SPDX License");
                    }
                    // the url was not valid, so it is not a valid SPDX License
                    else{
                        r = new Result(TEST_NAME, Result.STATUS.FAIL, "Invalid " +
                                "SPDX License");
                    }
                    r.addContext(c, "license");
                    results.add(r);
                    // reset link string builder
                    link.setLength(0);
                }
                // an issue with the url connection or link occurred
                catch(IOException e){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL, "Invalid " +
                            "SPDX License");
                    r.addContext(c, "license");
                    results.add(r);
                    link.setLength(0);
                }
            }
        }
        return results;
    }
}
