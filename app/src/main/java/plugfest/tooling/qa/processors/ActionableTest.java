package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.test_results.Test;
import plugfest.tooling.qa.test_results.TestResults;
import plugfest.tooling.sbom.Component;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * File: ActionableTest.java
 * Test fields to ensure data contained is usable. (not placeholder, not null, not invalid, etc.)
 *
 * @author Asa Horn
 */

public class ActionableTest extends MetricTest{
    private final String CPE_LOOKUP_URL;

    private boolean undefinedBehavior;

    private boolean failed;

    public ActionableTest() {
        super("Actionable Test");

        //one result per page because we just care about the response code (404 vs 200)
        CPE_LOOKUP_URL = "https://services.nvd.nist.gov/rest/json/cpes/2.0?resultsPerPage=1&cpeMatchString=";
        PURL_LOOKUP_URL = "https://purl.org/"; //todo implement purl lookup
        SWID_LOOKUP_URL = "https://swidtag.org/"; //todo implement swid lookup

        undefinedBehavior = false;
        failed = false;
    }
    @Override
    public TestResults test(Component c) {
        // Init StringBuilder
        TestResults testResults = new TestResults(c);

        // Preform subtests
        testResults.addTest(testUniqueIdentifiers(c));
        //testResults.addTest(testTimestamp(c));

        // Return result
        return testResults;
    }

    private Test testUniqueIdentifiers(Component c) {
        /*
         * the UUID object has multiple UUIDs in it. If one fails the lookup, then the entire test fails.
         * If there are no fails, and at least one undefined, then the test is undefined.
         * If all of them pass, then the test passes.
         */

        boolean undefinedBehavior = false;
        boolean failed = false;
        ArrayList<String> messages = new ArrayList<String>();

        for(String id : c.getCPE()) { //todo support other identifiers.
            int returnCode;

            //CPE match
            if(cpe23Regex.matcher(id.strip()).matches()) {
                if(tryURL(CPE_LOOKUP_URL) != 200){
                    //The URL is not reachable, so this test is undefined.
                    undefinedBehavior = true;
                    messages.add("UNDEFINED: CPE identifier " + id + " may or may not be valid. The CPE lookup service is not reachable at this time.");
                }

                returnCode = tryURL(CPE_LOOKUP_URL + id);

                //PURL match
            } else if(purlRegex.matcher(id.strip()).matches()) {
                //todo implement purl lookup

                returnCode = 0;

                //we have no idea what this is.
            } else {
                messages.add("UNDEFINED: The identifier " + id + " is not in a known format (CPE, SWID, PURL).");
                undefinedBehavior = true;
                continue;
            }

        }

        return new Test(false, "because I feel like it");
    }

    private int tryURL(String url) {
        try {
            URL u = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) u.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();
            return connection.getResponseCode();
        } catch (IOException e) {
            return -1;
        }
    }

    private String getMessage(int status) {
        if(status == 200) { //we found some result with this tag. Note that we don't check if the tag referenced the correct component.
            return("PASS: %s identifier is registered in the database.");
        } else if(status == 404) { //the tag does not exist, we fail this metric because we can't use a tag which doesn't exist.
            this.failed = true;
            return("FAIL: %s identifier is not registered in the database.");
        } else if (status == -1) { //something went funky and the service stopped responding between the first "are you alive" request and second request.
            this.undefinedBehavior = true;
            return("UNDEFINED: %s identifier may or may not be valid. The lookup service was reachable but did not respond to the lookup request.");
        } else { //something went wrong, and we got a response code we don't know how to handle.
            this.undefinedBehavior = true;
            return("UNDEFINED: %s identifier may or may not be valid. The lookup service returned an unexpected response code: " + returnCode);
        }
    }
}
