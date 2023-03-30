package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.SBOM;

public class IntrinsicProcessor extends AttributeProcessor {
    public IntrinsicProcessor() {
        super(new MetricTest[]{
                // Add new tests here
        });
    }
}
