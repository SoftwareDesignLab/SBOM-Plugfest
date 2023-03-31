package plugfest.tooling.qa.processors;

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

        // Regex101: https://regex101.com/r/wzJeIq/2
        // Checks if version is in form: "1.*" | "1:*", version format varies a lot
        this.componentVersionRegex = Pattern.compile("^([0-9]+(?:\\.|:).*)", Pattern.MULTILINE);

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
    public ArrayList<String> test(Component c){
        // Init StringBuilder
        final ArrayList<String> testResults = new ArrayList<>();
        final String UUIDShort = c.getUUID().toString().substring(0, 5);

        // Check completeness of publisher name
        if(!this.publisherNameRegex.matcher(c.getPublisher().strip()).matches())
            testResults.add(String.format("FAILED: Component %s Publisher Name is Not Complete: %s", UUIDShort, c.getPublisher()));

        // Check completeness of component name
        if(c.getName().isBlank()) {
            testResults.add(String.format("FAILED: Component %s Name is Not Complete: %s", UUIDShort, c.getName()));
        }

        // Check completeness of component version
        if(!this.componentVersionRegex.matcher(c.getVersion().strip()).matches()) {
            testResults.add(String.format("FAILED: Component %s Version is Not Complete: %s", UUIDShort, c.getVersion()));
        }

        int invalid;

        // Check CPEs and return a number of invalid CPEs per component
        invalid = getNumInvalidStrings(c.getCPE(), cpe23Regex);
        if(invalid > 0) { // If there are invalid cpes, mark as failed
            testResults.add(String.format("FAILED: Component %s had %d CPE(s) with Invalid Format", UUIDShort, invalid));
        }

        // Check PURLs and return a number of invalid PURLs
        invalid = getNumInvalidStrings(c.getPURL(), purlRegex);
        if(invalid > 0) { // If there are invalid PURLs, mark as failed
            testResults.add(String.format("FAILED: Component %s had %d PURL(s) with Invalid Format", UUIDShort, invalid));
        }

        // If no checks failed, mark test as passed
        if(testResults.isEmpty()) testResults.add("PASSED");

        // Return result
        return testResults;
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
            if(!regex.matcher(s.strip()).matches())
                stringCounter++;
        }
        return stringCounter;
    }
}
