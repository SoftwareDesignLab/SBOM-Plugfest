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
    private final Pattern cpe23Regex;
    private final Pattern purlRegex;

    private final String CPE_LOOKUP_URL;
    public ActionableTest() {
        super("Actionable Test");

        String CPE_LOOKUP_URL1; //not sure what is going on here. I am trying to wrap up for the day. Will check it out tomorrow.

        //todo this is repeated from completeness test, which is not ideal.
        //define strings
        this.cpe23Regex = Pattern.compile("cpe:2\\.3:[aho\\*\\-](:(((\\?*|\\*?)([a-zA-Z0-9\\-\\._]|(\\\\[\\\\\\*\\?" +
                "!\"#$$%&'\\(\\)\\+,/:;<=>@\\[\\]\\^`\\{\\|}~]))+(\\?*|\\*?))|[\\*\\-])){5}(:(([a-zA-Z]{2,3}" +
                "(-([a-zA-Z]{2}|[0-9]{3}))?)|[\\*\\-]))(:(((\\?*|\\*?)([a-zA-Z0-9\\-\\._]|(\\\\[\\\\\\*\\?!\"#$$%&'" +
                "\\(\\)\\+,/:;<=>@\\[\\]\\^`\\{\\|}~]))+(\\?*|\\*?))|[\\*\\-])){4}", Pattern.MULTILINE);

        this.purlRegex = Pattern.compile("^pkg:([a-zA-Z][a-zA-Z0-9-~._%]*\\/)+[a-zA-Z][a-zA-Z0-9-~._%]*(@(" +
                "[a-zA-Z0-9-~._%]+))?(\\?(([a-zA-Z][a-zA-Z0-9_.-]*=.+)&)*([a-zA-Z][a-zA-Z0-9-~._%]*=.+))?(#(" +
                "[a-zA-Z0-9-~._%]*\\/)+[a-zA-Z0-9-~._%]*)?", Pattern.MULTILINE);

        CPE_LOOKUP_URL1 = CPE_LOOKUP_URL1 = "https://services.nvd.nist.gov/rest/json/cpes/2.0?resultsPerPage=1&cpeMatchString=";
        CPE_LOOKUP_URL = CPE_LOOKUP_URL1;
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

            //todo I was mistaken about how this was laied out. This code is most likely going to go in a function called by several loops, one for each ID type

            if(returnCode == 200) {
                messages.add("PASS: " + id + " identifier is registered in the database.");
            } else if(returnCode == 404) {
                failed = true;
                messages.add("FAIL: " + id + " identifier is structurally valid, but is not registered in the database.");
            } else if (returnCode == -1) {
                undefinedBehavior = true;
                messages.add("UNDEFINED: " + id + " identifier may or may not be valid. The lookup service was reachable but did not respond to the lookup request.");
            } else {
                undefinedBehavior = true;
                messages.add("UNDEFINED: " + id + " identifier may or may not be valid. The lookup service returned an unexpected response code: " + returnCode);
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
}
