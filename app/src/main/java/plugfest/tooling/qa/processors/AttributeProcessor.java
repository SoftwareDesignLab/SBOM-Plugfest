package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.SBOM;

public interface AttributeProcessor {
    QualityReport process(SBOM sbom);
}
