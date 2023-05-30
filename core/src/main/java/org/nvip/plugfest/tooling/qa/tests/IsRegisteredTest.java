package org.nvip.plugfest.tooling.qa.tests;


import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.uids.PURL;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * file: IsRegisteredTest.java
 *
 * Test each component in a given SBOM if it is registered with a
 * given package manager through its PURL
 */
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

    private List<Result> testComponentPURLs(Component c){
        List<Result> purlResults = new ArrayList<>();
        Result r;

        // get all the purls for the given component
        Set<PURL> purls = c.getPurls();
        // if no purls are present, the test automatically fails
        if(purls.isEmpty()){
            r = new Result(TEST_NAME, Result.STATUS.ERROR,
                    "Component has no PURL");
            r.addContext(c, "PURL Validation");
            purlResults.add(r);
        }
        else{
            // check all purl based on its type
            for(PURL p : c.getPurls()){
                // holds the response code from the purl
                int response = 0;
                try{
                    // extract method based on package manager type
                    switch(p.getType().toLowerCase()) {
                        // TODO More cases need to be added for package manager types
                        case "maven" -> response =  extractFromMaven(p);
                        case "pypi" -> response = extractFromPyPi(p);
                        case "nuget" -> response = extractFromNuget(p);
                        case "cargo" -> response = extractFromCargo(p);
                        case "golang" -> response = extractFromGo(p);
                        case "npm" -> response = extractFromNPM(p);
                        case "composer" -> response = extractFromComposer(p);
                        case "gem" -> response = extractFromGem(p);
                        // an invalid package manager type
                        default -> {
                            r = new Result(TEST_NAME, Result.STATUS.ERROR,
                                    "Package Manager is not valid");
                            r.addContext(c, "PURL Validation");
                            purlResults.add(r);
                        }
                    }
                }catch(IOException e){
                    r = new Result(TEST_NAME, Result.STATUS.ERROR,
                            "PURL had an error");
                    r.addContext(c, "PURL Validation");
                    purlResults.add(r);
                }

                if(response == 0){
                    continue;
                }
                // if the response code is 200 (HTTP_OK), then
                // package manager is valid
                else if(response == HttpURLConnection.HTTP_OK){
                    r = new Result(TEST_NAME, Result.STATUS.PASS,
                            "Package is registered with package " +
                                    "manager");
                } else{
                    r = new Result(TEST_NAME, Result.STATUS.FAIL,
                            "Package is not registered with " +
                                    "package manager");
                }
                r.addContext(c, "PURL Validation");
                purlResults.add(r);

            }
        }
        return purlResults;
    }

    /**
     * Extract data for maven based packages.
     * Source: <a href="https://mvnrepository.com/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with the PURL
     * @throws IOException issue with http connection
     */
    private int extractFromMaven(PURL p) throws IOException{
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
    private int extractFromPyPi(PURL p) throws IOException {
        // Query page
        URL url = new URL ("https://pypi.org/project/" +
                p.getName().toLowerCase() +
                (p.getVersion() != null ? "/" + p.getVersion() : ""));
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        // get the response code from this url
        return huc.getResponseCode();
    }

    /**
     * Extract data from C# NuGet based packages
     * Source: <a href="https://www.nuget.org/packages/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with PURL info
     * @throws IOException issue with http connection
     */
    private int extractFromNuget(PURL p) throws IOException{
        // Query nuget page
        // package name is required, add the version if it is
        // included in the purl
        URL url = new URL ("https://www.nuget.org/packages/" +
                p.getName().toLowerCase() +
                (p.getVersion() != null ? "/" + p.getVersion() : ""));
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        // get the response code from this url
        return huc.getResponseCode();
    }

    /**
     * Extract data from Rust Cargo based packages
     * Source: <a href="https://crates.io/crates/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with PURL info
     * @throws IOException issue with http connection
     */
    private int extractFromCargo(PURL p) throws IOException{
        // Query cargo page
        // package name is required, add the version if it is
        // included in the purl
        URL url = new URL ("https://crates.io/crates/" +
                p.getName().toLowerCase() +
                (p.getVersion() != null ? "/" + p.getVersion() : ""));
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        // get the response code from this url
        return huc.getResponseCode();
    }
    /**
     * Extract data from Golang based packages
     * Source: <a href="https://proxy.golang.org/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with PURL info
     * @throws IOException issue with http connection
     */
    private int extractFromGo(PURL p) throws IOException{
        // Query go page
        // package name and version are required
        URL url = new URL ("https://proxy.golang.org/" +
                p.getName().toLowerCase() + "@v/" +
                p.getVersion() + ".info");
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        // get the response code from this url
        return huc.getResponseCode();
    }

    /**
     * Extract data from Node NPM based packages
     * Source: <a href="https://registry.npmjs.org/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with PURL info
     * @throws IOException issue with http connection
     */
    private int extractFromNPM(PURL p) throws IOException{
        // Query npm registry page
        // package name is required, add the version if it is
        // included in the purl
        URL url = new URL ("https://registry.npmjs.org/" +
                p.getName().toLowerCase() +
                (p.getVersion() != null ? "/" + p.getVersion() : ""));
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        // get the response code from this url
        return huc.getResponseCode();
    }

    /**
     * Extract data from Composer PHP based packages
     * Source: <a href="https://packagist.org/packages/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with PURL info
     * @throws IOException issue with http connection
     */
    private int extractFromComposer(PURL p) throws IOException{

        // concat namespace for the url
        String namespace = String.join("/", p.getNamespace());

        // Query composer packages page
        // package namespace and name is required, add the version if it is
        // included in the purl
        URL url = new URL ("https://packagist.org/packages/" +
                namespace + "/" +
                p.getName().toLowerCase() +
                (p.getVersion() != null ? "#v" + p.getVersion() : ""));
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        // get the response code from this url
        return huc.getResponseCode();
    }
    /**
     * Extract data from Rubygems Gem based packages
     * Source: <a href="https://rubygems.org/gems/">...</a>
     * @param p purl to use to query for info
     * @return an int response code when opening up a connection with PURL info
     * @throws IOException issue with http connection
     */
    private int extractFromGem(PURL p) throws IOException{
        // Query Gem packages page
        // package name is required, add the version if it is
        // included in the purl
        URL url = new URL ("https://rubygems.org/gems/" +
                p.getName().toLowerCase() + "/" +
                (p.getVersion() != null ? "versions/" + p.getVersion() : ""));
        HttpURLConnection huc = (HttpURLConnection) url.openConnection();
        // get the response code from this url
        return huc.getResponseCode();
    }

}
