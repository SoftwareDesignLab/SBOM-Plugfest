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
        // TODO: Regex
        if(!Pattern.compile("componentPublisherRegex").matcher(c.getPublisher()).matches())
            testResult.append(String.format("FAILED: Component %s Publisher Name is Not Complete", UUIDShort));
        // Check accuracy of component version
        // TODO: Regex
        if(!Pattern.compile("componentNameRegex").matcher(c.getName()).matches()) {
            // Add separator if not first check to fail
            if(!testResult.isEmpty()) testResult.append("\n");
            testResult.append(String.format("FAILED: Component %s Name is Not Complete", UUIDShort));
        }
        // Check accuracy of component name
        // TODO: Regex
        if(!Pattern.compile("componentVersionRegex").matcher(c.getVersion()).matches()) {
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
