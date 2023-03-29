package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.SBOM;

public class SchemaProcessor implements AttributeProcessor{
    // todo, int schema specific processor? is this even needed?
    @Override
    public QualityReport process(SBOM sbom) {
        return null;
    }
}
