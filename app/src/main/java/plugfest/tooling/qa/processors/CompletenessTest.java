package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.test_results.Test;
import plugfest.tooling.qa.test_results.TestResults;
import plugfest.tooling.sbom.Component;

import java.util.ArrayList;
import java.util.Set;
import java.util.regex.Pattern;

public class CompletenessTest extends MetricTest {
    private final Pattern publisherNameRegex;
    private final Pattern componentVersionRegex;

    /**
     * CPE v2.3 regex string
     */
    private final Pattern cpe23Regex;
    private final Pattern purlRegex;

    protected CompletenessTest() {
        super("Completeness Test");
        // Regex101: https://regex101.com/r/KNxGCb/3
        // Checks if name is in form: "Person: First Last <email@mail.com>"
        this.publisherNameRegex = Pattern.compile("^Person: ([\\w äöüÄÖÜß]*) <(.*)>", Pattern.MULTILINE);

        // Regex101: https://regex101.com/r/wzJeIq/4
        // Checks if version is in form: "12.*" | "4:*", version format varies a lot
        this.componentVersionRegex = Pattern.compile("^([0-9]+[\\.:\\-].*)", Pattern.MULTILINE);

        // TODO for these patterns: check if name, version, etc matches component name, version, etc. Make classes?
        // Official CPE Schema: https://csrc.nist.gov/schema/cpe/2.3/cpe-naming_2.3.xsd
        this.cpe23Regex = Pattern.compile("cpe:2\\.3:[aho\\*\\-](:(((\\?*|\\*?)([a-zA-Z0-9\\-\\._]|(\\\\[\\\\\\*\\?" +
                "!\"#$$%&'\\(\\)\\+,/:;<=>@\\[\\]\\^`\\{\\|}~]))+(\\?*|\\*?))|[\\*\\-])){5}(:(([a-zA-Z]{2,3}" +
                "(-([a-zA-Z]{2}|[0-9]{3}))?)|[\\*\\-]))(:(((\\?*|\\*?)([a-zA-Z0-9\\-\\._]|(\\\\[\\\\\\*\\?!\"#$$%&'" +
                "\\(\\)\\+,/:;<=>@\\[\\]\\^`\\{\\|}~]))+(\\?*|\\*?))|[\\*\\-])){4}", Pattern.MULTILINE);

        // Regex101: https://regex101.com/r/vp2Hk0/1 (regex takes forever to write)
        // Official PURL spec: https://github.com/package-url/purl-spec/blob/master/PURL-SPECIFICATION.rst
        this.purlRegex = Pattern.compile("pkg:([a-zA-Z][a-zA-Z0-9-~._%]*\\/)+[a-zA-Z][a-zA-Z0-9-~._%]*(@(" +
                "[a-zA-Z0-9-~._%]+))?(\\?(([a-zA-Z][a-zA-Z0-9_.-]*=.+)&)*([a-zA-Z][a-zA-Z0-9-~._%]*=.+))?(#(" +
                "[a-zA-Z0-9-~._%]*\\/)+[a-zA-Z0-9-~._%]*)?", Pattern.MULTILINE);
    }

    @Override
    public TestResults test(Component c){
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

    private Test testPublisherName(Component c) {
        // Check completeness of publisher name
        if(c.getPublisher() == null ||
                !this.publisherNameRegex.matcher(c.getPublisher().strip()).matches())
            return new Test(false, "Publisher Name is Not Complete '", c.getPublisher(), "'.");
//            return String.format("FAILED Component %s: Publisher Name is Not Complete: '%s'", c.getName(), c.getPublisher());
        return new Test(true, "Publisher Name is Complete.");
    }

    private Test testComponentName(Component c) {
        // Check completeness of component name
        if(c.getName().isBlank())
            return new Test(false, "Name is Not Complete: '", c.getName(), "'.");
//            return String.format("FAILED Component %s: Name is Not Complete: '%s'", c.getName(), c.getName());
        return new Test(true, "Name is Complete.");
    }

    private Test testComponentVersion(Component c) {
        // Check completeness of component version
        if(!this.componentVersionRegex.matcher(c.getVersion().strip()).matches())
            return new Test(false, "Version is Not Complete: '", c.getVersion(), "'.");
//            return String.format("FAILED Component %s: Version is Not Complete: '%s'", c.getName(), c.getVersion());
        return new Test(true, "Version is Complete.");
    }

    private Test testCPEs(Component c) {
        // Check CPEs and return a number of invalid CPEs per component
        final int invalid = getNumInvalidStrings(c.getCPE(), cpe23Regex);
        if(invalid > 0) // If there are invalid CPEs, mark as failed
            return new Test(false, "Had ", Integer.toString(invalid), " CPE(s) with Invalid Format.");
//            return String.format("FAILED Component %s: Had %d CPE(s) with Invalid Format", c.getName(), invalid);
        return new Test(true, "CPE(s) have Valid Format.");
    }

    private Test testPURLs(Component c) {
        // Check PURLs and return a number of invalid PURLs
        final int invalid = getNumInvalidStrings(c.getPURL(), purlRegex);
        if(invalid > 0) // If there are invalid PURLs, mark as failed
            return new Test(false, "Had ", Integer.toString(invalid), " PURL(s) with Invalid Format.");
//            return String.format("FAILED Component %s: Had %d PURL(s) with Invalid Format", c.getName(), invalid);
        return new Test(true, "PURL(s) have Valid Format.");
    }

    /**
     * Private helper method to get a number of strings in a set that do not match a given regex
     * @param strings The set of strings to match with the regex
     * @param regex The regex to match
     * @return The number of strings that do not match the regex
     */
    private int getNumInvalidStrings(Set<String> strings, Pattern regex) {
        int stringCounter = 0;

        for(String s : strings) { // Loop through all strings and match regex
            if(s != null &&
                    !regex.matcher(s.strip()).matches())
                stringCounter++;
        }
        return stringCounter;
    }
}
