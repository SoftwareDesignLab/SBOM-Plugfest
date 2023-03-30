package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.Component;
import plugfest.tooling.sbom.SBOM;

import java.util.HashSet;
import java.util.Set;

public class ContextualProcessor implements AttributeProcessor{
    private Set<MetricTest> tests = new HashSet<>() {{
       add(new CompletenessTest());
    }};
    @Override
    public QualityReport process(SBOM sbom) {
        // Init quality report
        QualityReport qr = new QualityReport();

        // Run all components through each test individually
        final MetricTest[] metricTests = this.tests.toArray(new MetricTest[0]);
        for (int i = 0; i < metricTests.length; i++) {
            // Store current test in local variable
            final MetricTest mt = metricTests[i];

            // Test each component against current test
            final Component[] components = sbom.getAllComponents().toArray(new Component[0]);
            for (int j = 0; j < components.length; j++) {
                // Store current component in local variable
                final Component c = components[i];
                // Test and append results
                mt.addTestResult(mt.test(c));
                // Add separating newline if not last Component in array
                if(j + 1 < components.length) mt.addTestResult("\n");
            }

            // Add test results
            qr.addTestResult(mt.getName(), mt.getTestResults());
        }

        // Return built QualityReport
        return qr;
    }
}
