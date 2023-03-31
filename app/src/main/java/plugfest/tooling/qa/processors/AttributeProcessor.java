package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.Component;
import plugfest.tooling.sbom.SBOM;

import java.util.*;

public abstract class AttributeProcessor {
    private final Set<MetricTest> tests;

    public AttributeProcessor(MetricTest[] metricTests) {
        this.tests = new HashSet<>();
        this.tests.addAll(List.of(metricTests));
    }

    public QualityReport process(SBOM sbom) {
        // Init quality report
        QualityReport qr = new QualityReport();

        // Run all Components through each test individually
        final MetricTest[] metricTests = this.tests.toArray(new MetricTest[0]);
        for (int i = 0; i < metricTests.length; i++) {
            // Store current test in local variable
            final MetricTest mt = metricTests[i];

            // Test each Component against current test
            final Component[] components = sbom.getAllComponents().toArray(new Component[0]);
            for (int j = 0; j < components.length; j++) {
                // Store current Component in local variable
                final Component c = components[j];
                if(c.isUnpackaged()) continue;
                // Test and append results
                final ArrayList<String> testResults = mt.test(c);
                // If no checks failed, mark test as passed
                if(testResults.isEmpty()) testResults.add(String.format("PASSED Component %s", c.getName()));
                mt.addTestResults(testResults);
            }

            // Add test results
            qr.addTestResult(mt.getName(), mt.getTestResults());
        }

        // Return built QualityReport
        return qr;
    }
}
