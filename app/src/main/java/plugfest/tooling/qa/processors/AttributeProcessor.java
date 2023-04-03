package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.qa.test_results.TestResults;
import plugfest.tooling.sbom.Component;
import plugfest.tooling.sbom.SBOM;

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

        // Run all Components through each test individually
        final Component[] components = sbom.getAllComponents().toArray(new Component[0]);

        for(Component c : components) { // Loop through all components in SBOM
            if(c.isUnpackaged()) continue; // Skip component if it's a local file

            // Get array of MetricTests to perform
            final MetricTest[] metricTests = this.tests.toArray(new MetricTest[0]);

            // Store TestResults for each component
            TestResults results = new TestResults(c);

            // Loop through all MetricTests
            for(MetricTest mt : metricTests){
                results.addTests(mt.test(c)); // Add the TestResults from each metric to the component TestResults
            }

            // Add test results to QualityReport
            qr.addTestResult(results);
        }

        // Return built QualityReport
        return qr;
    }
}
