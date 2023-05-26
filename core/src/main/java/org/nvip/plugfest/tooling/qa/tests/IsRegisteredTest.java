package org.nvip.plugfest.tooling.qa.tests;

import org.nvip.plugfest.tooling.qa.oldQA.DataVerificationTest;
import org.nvip.plugfest.tooling.qa.oldQA.test_results.Test;
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
    @Override
    public List<Result> test(SBOM sbom) {
        List<Result> results = new ArrayList<>();
        for(Component c : sbom.getAllComponents()){
            results.addAll(testComponentPURLs(c));
        }
        return results;
    }

    public List<Result> testComponentPURLs(Component c){
        List<Result> purlResults = new ArrayList<>();
        Result r;

        // get all the purls for the given component
        Set<PURL> purls = c.getPurls();
        // if no purls are present, the test automatically fails
        if(purls.isEmpty()){
            r = new Result(TEST_NAME, Result.STATUS.FAIL, "Component has no PURL");
            r.addContext(c, "PURL Validation");
            purlResults.add(r);
            return purlResults;
        }
        else{
            // check the purl based on its type
            for(PURL p : c.getPurls()){
                int response;
                try{
                    switch(p.getType().toLowerCase()) {
                        case "maven" -> response =  extractFromMaven(p);
                    }
                }catch(IOException e){

                }

            }
            return purlResults;
        }
    }

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
}
