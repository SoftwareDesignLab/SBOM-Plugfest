package org.nvip.plugfest.tooling.qa.tests;


import jregex.Matcher;
import jregex.Pattern;
import jregex.REFlags;
import org.apache.commons.io.IOUtils;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

/**
 * file: ValidSPDXLicenseTest.java
 *
 * Test each component in the given SBOM if the license is an existing
 * valid SPDX License
 * @author Matthew Morrison
 * @author Derek Garcia
 */
public class ValidSPDXLicenseTest extends MetricTest{

    private static final String TEST_NAME = "ValidSPDXLicense";
    private static final String SPDX_LICENSE_LIST_URL = "https://spdx.org/licenses/";
    // Regex to isolate License name table
    private static final String SPDX_NAME_TABLE_REGEX = "<table class=\"sortable\">[\\s\\S]*?<tbody>([\\s\\S]*)<\\/tbody>";

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
            // Skip if no licenese
            if(isEmptyOrNull(c.getLicenses()))
                continue;

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
        Result r = null;

        // for every license id
        //TODO only held as a string. A License object should be created
        for(String l : c.getLicenses()){
            // First attempt to use license as identifier
            try {
                // Ping endpoint
                URL url = new URL(SPDX_LICENSE_LIST_URL + l);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("GET");

                // valid SPDX License Identifier
                if(huc.getResponseCode() == HttpURLConnection.HTTP_OK)
                    r = new Result(TEST_NAME, Result.STATUS.PASS, "Valid SPDX License Identifier");
            }
            // an issue with the url connection or link occurred
            catch(Exception e){
                r = new Result(TEST_NAME, Result.STATUS.FAIL, "Invalid SPDX License Identifier");

            } finally {
                // update r and add to results
                assert r != null;
                r.updateInfo(Result.Context.STRING_VALUE, l);
                r.addContext(c, "license");
                results.add(r);
            }

            // Second attempt to use license as name
            try {
                // Ping endpoint
                URL url = new URL(SPDX_LICENSE_LIST_URL);
                HttpURLConnection huc = (HttpURLConnection) url.openConnection();
                huc.setRequestMethod("GET");

                // valid SPDX License Identifier
                if(huc.getResponseCode() == HttpURLConnection.HTTP_OK){
                    // Get HTML
                    InputStream in = huc.getInputStream();
                    String encoding = huc.getContentEncoding();
                    encoding = encoding == null ? "UTF-8" : encoding;
                    String html = IOUtils.toString(in, encoding);

                    // Create table regex
                    Pattern p = new Pattern(SPDX_NAME_TABLE_REGEX, REFlags.MULTILINE);
                    Matcher m = p.matcher(html);

                    // Search for License name
                    if(m.find() && m.group(0).contains(">" + l + "<")){
                        r = new Result(TEST_NAME, Result.STATUS.PASS, "Valid SPDX License Name");
                    } else {
                        r = new Result(TEST_NAME, Result.STATUS.FAIL, "Invalid SPDX License Name");
                    }

                // Issue querying SPDX License page
                } else {
                    r = new Result(TEST_NAME, Result.STATUS.ERROR, "Couldn't query " + SPDX_LICENSE_LIST_URL);
                }

            }
            // an issue with the url connection or link occurred
            catch(Exception e){
                r = new Result(TEST_NAME, Result.STATUS.FAIL, "Invalid SPDX License Identifier");

            } finally {
                // update r and add to results
                r.updateInfo(Result.Context.STRING_VALUE, l);
                r.addContext(c, "license");
                results.add(r);
            }
        }

        return results;
    }
}
