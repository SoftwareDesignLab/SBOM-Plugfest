package plugfest.tooling.qa.processors;

import plugfest.tooling.sbom.Component;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class CompletenessTest extends MetricTest {
    private final Pattern publisherNameRegex;
    private final Pattern componentVersionRegex;

    protected CompletenessTest() {
        super("Completeness Test");
        // Regex101: https://regex101.com/r/KNxGCb/3
        // Checks if name is in form: "Person: First Last <email@mail.com>"
        this.publisherNameRegex = Pattern.compile("^Person: ([\\w äöüÄÖÜß]*) <(.*)>", Pattern.MULTILINE);

        // Regex101: https://regex101.com/r/wzJeIq/2
        // Checks if version is in form: "1.*" | "1:*", version format varies a lot
        this.componentVersionRegex = Pattern.compile("^([0-9]+(?:\\.|:).*)", Pattern.MULTILINE);
    }

    @Override
    public ArrayList<String> test(Component c){
        // Init StringBuilder
        final ArrayList<String> testResults = new ArrayList<>();
        final String UUIDShort = c.getUUID().toString().substring(0, 5);

        // Check completeness of publisher name
        if(!this.publisherNameRegex.matcher(c.getPublisher().strip()).matches())
            testResults.add(String.format("FAILED: Component %s Publisher Name is Not Complete", UUIDShort));

        // Check completeness of component name
        if(c.getName().isBlank()) {
            // Add separator if not first check to fail
            if(!testResults.isEmpty()) testResults.add("\n");
            testResults.add(String.format("FAILED: Component %s Name is Not Complete", UUIDShort));
        }

        // Check completeness of component version
        if(!this.componentVersionRegex.matcher(c.getVersion().strip()).matches()) {
            // Add separator if not first check to fail
            if(!testResults.isEmpty()) testResults.add("\n");
            testResults.add(String.format("FAILED: Component %s Version is Not Complete", UUIDShort));
        }

        // If no checks failed, mark test as passed
        if(testResults.isEmpty()) testResults.add("PASSED");

        // Return result
        return testResults;
    }
}
