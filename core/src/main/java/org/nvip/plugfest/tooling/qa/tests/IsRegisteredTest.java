package org.nvip.plugfest.tooling.qa.tests;


import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class IsRegisteredTest extends MetricTest{

    private static final String TEST_NAME = "IsRegistered";

    /**
     * Run the test for all components in the SBOM
     * @param sbom the SBOM to test
     * @return a collection of results given from the tests
     */
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();
        // for every component, test for purls and if they are valid
        for(Component c : sbom.getAllComponents()){
            results.addAll(testComponentPURLs(c));
        }
        // return findings of tests
        return results;
    }

    public List<Result> testComponentPURLs(Component c){
        List<Result> purlResults = new ArrayList<>();
        Result r;

        // get all the purls for the given component
        Set<PURL> purls = c.getPurls();
        // if no purls are present, the test automatically fails
        if(purls.isEmpty()){
            r = new Result(TEST_NAME, Result.STATUS.FAIL,
                    "Component has no PURL");
            r.addContext(c, "PURL Validation");
            purlResults.add(r);
            return purlResults;
        }
        else{
            // check all purl based on its type
            for(PURL p : c.getPurls()){
                // holds the response code from the purl
                int response = 0;
                try{
                    switch(p.getType().toLowerCase()) {
                        // TODO More cases need to be added for PURL types
                        case "maven" -> response =  extractFromMaven(p);
                        case "pypi" -> response = extractFromPyPi(p);
                        // an invalid PURL type
                        default -> {
                            r = new Result(TEST_NAME, Result.STATUS.FAIL,
                                    "PURL is not a valid type");
                            r.addContext(c, "PURL Validation");
                            purlResults.add(r);
                        }
                    }
                }catch(IOException e){
                    r = new Result(TEST_NAME, Result.STATUS.FAIL,
                            "PURL had an error");
                    r.addContext(c, "PURL Validation");
                    purlResults.add(r);
                }
                // if the response code is 200 (HTTP_OK), then
                // package manager is valid
                if(response == 200){
                    r = new Result(TEST_NAME, Result.STATUS.PASS,
                            "Package Manager is valid");
                    r.addContext(c, "PURL Validation");
                    purlResults.add(r);
                } else{
                    r = new Result(TEST_NAME, Result.STATUS.FAIL,
                            "Package Manager is invalid");
                    r.addContext(c, "PURL Validation");
                    purlResults.add(r);
                }

            }
            return purlResults;
        }
    }

    /**
     * Extract data for maven based packages.
     * Source: <a href="https://mvnrepository.com/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with the PURL
     * @throws IOException issue with http connection
     */
    public int extractFromMaven(PURL p) throws IOException{
        // maven requires namespace
        if(p.getNamespace() == null || p.getNamespace().size() == 0)
            return 404;

        // maven requires version
        if(p.getVersion() == null)
            return 404;

        // build namespace for request
        StringBuilder namespaceUrl = new StringBuilder();
        for(int i = 0; i < p.getNamespace().size(); i++)
            namespaceUrl.append(p.getNamespace().get(i).toLowerCase()).append("/");

        URL url = new URL ("https://mvnrepository.com/artifact/" +
                namespaceUrl +
                p.getName().toLowerCase() +
                "/" + p.getVersion());
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        // get the response code from this url
        return huc.getResponseCode();
    }

    /**
     * Extract data for python based packages.
     * Source: <a href="https://pypi.org/project/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with the PURL
     * @throws IOException issue with http connection
     */
    public int extractFromPyPi(PURL p) throws IOException {
        // Query page
        URL url = new URL ("https://pypi.org/project/" +
                p.getName().toLowerCase() +
                (p.getVersion() != null ? "/" + p.getVersion() : ""));
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        huc.setRequestMethod("GET");
        // get the response code from this url
        return huc.getResponseCode();
    }
}
