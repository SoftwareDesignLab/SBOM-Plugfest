package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.test_results.Test;
import plugfest.tooling.qa.test_results.TestResults;
import plugfest.tooling.sbom.Component;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * File: CompletenessTest.java
 * An instance of CompletenessTest tests the publisher name, component name, component version, all CPEs, and all PURLs
 * to determine if they are not empty and formatted correctly (if applicable).
 *
 * @author Dylan Mulligan
 * @author Ian Dunn
 */
public class CompletenessTest extends MetricTest {

    /**
     * The Regex used to test the format of a component publisher name.
     */
    private final Pattern publisherNameRegex;

    /**
     * The Regex used to test the format of a component version.
     */
    private final Pattern componentVersionRegex;

    /**
     * The Regex used to test the format of a CPE v2.3 identifier.
     */
    private final Pattern cpe23Regex;

    /**
     * The Regex used to test the format of a PURL string.
     */
    private final Pattern purlRegex;

    protected CompletenessTest() {
        super("Completeness Test"); // Test name

        /*
            Regex101: https://regex101.com/r/lgUcTP/1
            Checks if name is in form: "Person: First Last <email@mail.com>"
         */
        this.publisherNameRegex = Pattern.compile("^Person: (\\S+ )+<\\S*@\\S*\\.\\S*>$", Pattern.MULTILINE);

        /*
            Regex101: https://regex101.com/r/wzJeIq/4
            Checks if version is in form: "12.*" | "4:*", version format varies a lot
         */
        this.componentVersionRegex = Pattern.compile("^([0-9]+[\\.:\\-].*)", Pattern.MULTILINE);

        // TODO for these patterns: check if name, version, etc matches component name, version, etc. Make classes?

        /*
            Official CPE Schema: https://csrc.nist.gov/schema/cpe/2.3/cpe-naming_2.3.xsd
         */
        this.cpe23Regex = Pattern.compile("cpe:2\\.3:[aho\\*\\-](:(((\\?*|\\*?)([a-zA-Z0-9\\-\\._]|(\\\\[\\\\\\*\\?" +
                "!\"#$$%&'\\(\\)\\+,/:;<=>@\\[\\]\\^`\\{\\|}~]))+(\\?*|\\*?))|[\\*\\-])){5}(:(([a-zA-Z]{2,3}" +
                "(-([a-zA-Z]{2}|[0-9]{3}))?)|[\\*\\-]))(:(((\\?*|\\*?)([a-zA-Z0-9\\-\\._]|(\\\\[\\\\\\*\\?!\"#$$%&'" +
                "\\(\\)\\+,/:;<=>@\\[\\]\\^`\\{\\|}~]))+(\\?*|\\*?))|[\\*\\-])){4}", Pattern.MULTILINE);

        /*
            Regex101: https://regex101.com/r/vp2Hk0/1 (i love writing regex!!!)
            Official PURL spec: https://github.com/package-url/purl-spec/blob/master/PURL-SPECIFICATION.rst
         */
        this.purlRegex = Pattern.compile("^pkg:([a-zA-Z][a-zA-Z0-9-~._%]*\\/)+[a-zA-Z][a-zA-Z0-9-~._%]*(@(" +
                "[a-zA-Z0-9-~._%]+))?(\\?(([a-zA-Z][a-zA-Z0-9_.-]*=.+)&)*([a-zA-Z][a-zA-Z0-9-~._%]*=.+))?(#(" +
                "[a-zA-Z0-9-~._%]*\\/)+[a-zA-Z0-9-~._%]*)?", Pattern.MULTILINE);
    }

    /**
     * Test a single Component's publisher name, component name, component version, all CPEs, and all PURLs
     * to determine if they are not empty and formatted correctly (if applicable).
     *
     * @param c The component to test for completeness
     * @return A TestResults instance containing the results of all the above tests
     */
    @Override
    public TestResults test(Component c) {
        // Init StringBuilder
        final TestResults testResults = new TestResults(c); // Init TestResults for this component

        // Test Publisher Name
        testResults.addTest(testPublisherName(c));

        // Test Component Name
        testResults.addTest(testComponentName(c));

        // Test Component Version
        testResults.addTest(testComponentVersion(c));

        // Test CPEs
        testResults.addTest(testCPEs(c));

        // Test PURLs
        testResults.addTest(testPURLs(c));

        // Return result
        return testResults;
    }

    /**
     * Helper method that tests to ensure the component publisher:
     * a) Exists and
     * b) Is in the format of the Regex specified in the constructor
     *
     * @param c The component to test the publisher of
     * @return A single Test instance, describing if the test passed or failed and its details.
     */
    private Test testPublisherName(Component c) {
        if (c.getPublisher() == null || // Check if publisher exists
                !this.publisherNameRegex.matcher(c.getPublisher().strip()).matches()) // Compare against Regex
            return new Test(false, "Publisher Name is Not Complete '", c.getPublisher(), "'."); // Test failed
        return new Test(true, "Publisher Name is Complete."); // Test passed
    }

    /**
     * Helper method that tests to ensure the component name exists.
     *
     * @param c The component to test the name of
     * @return A single Test instance, describing if the test passed or failed and its details.
     */
    private Test testComponentName(Component c) {
        if (c.getName().isBlank()) // Check if name exists
            return new Test(false, "Name is Not Complete: '", c.getName(), "'."); // Test failed
        return new Test(true, "Name is Complete."); // Test passed
    }

    /**
     * Helper method that tests to ensure the component version is in the format of the Regex specified
     * in the constructor.
     *
     * @param c The component to test the version of
     * @return A single Test instance, describing if the test passed or failed and its details.
     */
    private Test testComponentVersion(Component c) {
        if (!this.componentVersionRegex.matcher(c.getVersion().strip()).matches()) // Compare against Regex
            return new Test(false, "Version is Not Complete: '", c.getVersion(), "'."); // Test failed
        return new Test(true, "Version is Complete."); // Test passed
    }

    /**
     * Helper method that tests each CPE identifier string in the component to make sure it follows the format
     * of CPE v2.3. It includes in the test message how many CPE strings had an invalid format.
     *
     * @param c The component to test the CPEs of
     * @return A single Test instance, describing if the test passed or failed and how many CPEs were invalid.
     */
    private Test testCPEs(Component c) {
        // Check CPEs and return a number of invalid CPEs per component
        final int invalid = getNumInvalidStrings(c.getCPE(), cpe23Regex);
        if (invalid > 0) // If there are invalid CPEs, mark as failed
            return new Test(false, "Had ", Integer.toString(invalid), " CPE(s) with Invalid Format.");
        return new Test(true, "CPE(s) have Valid Format."); // Test passed
    }

    /**
     * Helper method that tests each PURL string in the component to make sure it follows the official PURL
     * specification. It includes in the test message how many PURL strings had an invalid format.
     *
     * @param c The component to test the PURLs of
     * @return A single Test instance, describing if the test passed or failed and how many PURLs were invalid.
     */
    private Test testPURLs(Component c) {
        // Check PURLs and return a number of invalid PURLs
        final int invalid = getNumInvalidStrings(c.getPURL(), purlRegex);
        if (invalid > 0) // If there are invalid PURLs, mark as failed
            return new Test(false, "Had ", Integer.toString(invalid), " PURL(s) with Invalid Format.");
        return new Test(true, "PURL(s) have Valid Format."); // Test passed
    }

    /**
     * Private helper method to get a number of strings in a set that do not match a given regex.
     *
     * @param strings The set of strings to match with the regex
     * @param regex   The regex to match
     * @return The number of strings that do not match the regex
     */
    private int getNumInvalidStrings(Set<String> strings, Pattern regex) {
        int stringCounter = 0;

        for (String s : strings) { // Loop through all strings and match regex
            if (s != null &&
                    !regex.matcher(s.strip()).matches())
                stringCounter++;
        }
        return stringCounter;
    }
}
