package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.QualityReport;
import plugfest.tooling.sbom.SBOM;

public class IntrinsicProcessor implements AttributeProcessor{

    private int accuracyTest(SBOM sbom){
        // test sbom fields for accuracy
        return 0;
    }

    private int objectivityTest(SBOM sbom){
        return 0;
    }

    private int reputationTest(SBOM sbom){
        return 0;
    }
    @Override
    public QualityReport process(SBOM sbom) {
        QualityReport qr = new QualityReport();
        qr.updateScore(accuracyTest(sbom));
        qr.updateScore(objectivityTest(sbom));
        qr.updateScore(reputationTest(sbom));
        return qr;
    }
}
