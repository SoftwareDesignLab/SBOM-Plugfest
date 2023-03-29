package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.Component;
import plugfest.tooling.sbom.SBOM;

import java.util.Set;
import java.util.regex.Pattern;

public class ContextualProcessor implements AttributeProcessor{
    private String appropriateAmountTest(Component c) {
        return "";
    }

    private String completenessTest(Component c){
        // Init StringBuilder
        final StringBuilder testResult = new StringBuilder();
        final String UUIDShort = c.getUUID().toString().substring(0, 5);

        // Check accuracy of supplier name
        // Regex101: https://regex101.com/r/KNxGCb/3
        // Checks if name is in form: "Person: First Last <email@mail.com>"
        if(!Pattern.compile("^Person: ([\\w äöüÄÖÜß]*) <(.*)>").matcher(c.getPublisher().strip()).matches())
            testResult.append(String.format("FAILED: Component %s Publisher Name is Not Complete", UUIDShort));

        // Check accuracy of component name
        if(c.getName().isBlank()) {
            // Add separator if not first check to fail
            if(!testResult.isEmpty()) testResult.append("\n");
            testResult.append(String.format("FAILED: Component %s Name is Not Complete", UUIDShort));
        }

        // Check accuracy of component version
        // Regex101: https://regex101.com/r/wzJeIq/1
        // Checks if version is in form: "1.*" | "1:*", version format varies a lot
        if(!Pattern.compile("^([0-9](?:.|:).*)").matcher(c.getVersion().strip()).matches()) {
            // Add separator if not first check to fail
            if(!testResult.isEmpty()) testResult.append("\n");
            testResult.append(String.format("FAILED: Component %s Version is Not Complete", UUIDShort));
        }

        // If no checks failed, mark test as passed
        if(testResult.isEmpty()) return "PASSED";

        // Return result
        return testResult.toString();
    }

    @Override
    public QualityReport process(SBOM sbom) {
        // Init quality report
        QualityReport qr = new QualityReport();

        // Init testing StringBuilders
        final StringBuilder completenessTestResults = new StringBuilder();
        final StringBuilder appropriateAmountTestResults = new StringBuilder();

        final Component[] components = sbom.getAllComponents().toArray(new Component[0]);

        for (int i = 0; i < components.length; i++) {
            // Store current component in local variable
            final Component c = components[i];

            // Test component fields for completeness
            completenessTestResults.append(completenessTest(c));
            // Add separating newline if not last component in array
            if(i + 1 < components.length) completenessTestResults.append("\n");


            // Test component fields for appropriate amount of data
            appropriateAmountTestResults.append(appropriateAmountTest(c));
            // Add separating newline if not last component in array
            if(i + 1 < components.length) appropriateAmountTestResults.append("\n");
        }

        qr.addTestResult("Completeness Test", completenessTestResults.toString());
        qr.addTestResult("Appropriate Amount Test", appropriateAmountTestResults.toString());

        return qr;
    }
}
