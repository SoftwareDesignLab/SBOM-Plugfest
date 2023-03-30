package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.Component;
import plugfest.tooling.sbom.SBOM;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public abstract class AttributeProcessor {
    private final Set<MetricTest> tests;

    public AttributeProcessor(MetricTest[] metricTests) {
        this.tests = new HashSet<>();
        this.tests.addAll(List.of(metricTests));
    }

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
                final Component c = components[j];
                // Test and append results
                mt.addTestResult(mt.test(c));
            }

            // Add test results
            qr.addTestResult(mt.getName(), mt.getTestResults());
        }

        // Return built QualityReport
        return qr;
    }
}
