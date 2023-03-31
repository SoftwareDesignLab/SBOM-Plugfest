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
//            if(c.isUnpackaged()) continue; // TODO: Uncomment

            // Test each Component against all metric tests
            final MetricTest[] metricTests = this.tests.toArray(new MetricTest[0]);

            for(MetricTest mt : metricTests){
                // Add test results
                qr.addTestResult(mt.getName(), mt.test(c));
            }
        }

//        for (int i = 0; i < components.length; i++) {
//            // Store current Component in local variable
//            final Component c = components[i];
//
//            // Test each Component against current test
//            final MetricTest[] metricTests = this.tests.toArray(new MetricTest[0]);
//
//            for (int j = 0; j < metricTests.length; j++) {
//                // Store current test in local variable
//                final MetricTest mt = metricTests[j];
//
//
////                if(c.isUnpackaged()) continue; // TODO: Uncomment
//                // Test and append results
//                final ArrayList<String> testResults = mt.test(c);
//                // If no checks failed, mark test as passed
//                if(testResults.isEmpty()) testResults.add(String.format("PASSED Component %s", c.getName()));
//                mt.addTestResults(testResults);
//
//                // Add test results
//                qr.addTestResult(mt.getName(), mt.getTestResults());
//
//            }
//        }

        // Return built QualityReport
        return qr;
    }
}
