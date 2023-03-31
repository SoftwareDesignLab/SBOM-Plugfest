package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.qa.test_results.TestResults;
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
        final Component[] components = sbom.getAllComponents().toArray(new Component[0]);

        for(Component c : components) {
            if(c.isUnpackaged()) continue;

            // Test each Component against all metric tests
            final MetricTest[] metricTests = this.tests.toArray(new MetricTest[0]);

            for(MetricTest mt : metricTests){
                // Add test results
                qr.addTestResult(mt.getName(), mt.test(c));
            }
        }

        // Return built QualityReport
        return qr;
    }
}
