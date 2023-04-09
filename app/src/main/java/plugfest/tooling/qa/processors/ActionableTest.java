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
    private final String PURL_LOOKUP_URL;
    private final String SWID_LOOKUP_URL;


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
         * the identifier objects have multiple identifiers in them. If one fails the lookup, then the entire test fails.
         * If there are no fails, and at least one undefined, then the test is undefined.
         * If all of them pass, then the test passes.
         */

        ArrayList<String> messages = new ArrayList<>();
        if (tryURL(CPE_LOOKUP_URL) == 200) {
            for (String id : c.getCPE()) {
                //go to the URL and see if it returns a 200 or 404, then get the human message for that status code.
                //the flags for the ultimate test result are updated by the getMessage method.
                messages.add(
                        String.format(
                                getMessage(tryURL(CPE_LOOKUP_URL + id))
                                , id) //add the ID to the message using string format
                );
            }
        } else {
            // the NIST API is not working, we can't do this test.
            messages.add("The CPE lookup service is currently unavailable. Please try again later.");
            undefinedBehavior = true;
        }

        //make a string with every message
        StringBuilder messageString = new StringBuilder();
        for(String message : messages){
            messageString.append('\t').append(message).append('\n');
        }

        //sort that string first by the result (Fails, then Undefines, then Passes), then alphabetically.

        //finally return the test result with the messages string.
        if (failed) {
            messageString.insert(0, "FAILURE: The test failed because one or more of the identifiers was not found in the databases. See checks below for more details.\n");
            return new Test(false, messageString.toString());
        } else if (undefinedBehavior) {
            messageString.insert(0, "UNDEFINED: The test was inconclusive for at least one of the identifiers. See checks below for more details.\n");
            return new Test(false, messageString.toString());
        } else {
            messageString.insert(0, "PASSING: The test passed because all identifiers were located in the databases.\n");
            return new Test(true, messageString.toString());
        }
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
