package org.nvip.plugfest.tooling.qa;

import org.nvip.plugfest.tooling.qa.processors.AttributeProcessor;
import org.nvip.plugfest.tooling.qa.processors.ContextualProcessor;
import org.nvip.plugfest.tooling.qa.processors.IntrinsicProcessor;
import org.nvip.plugfest.tooling.qa.processors.RepresentationProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.HashSet;
import java.util.Set;

public class QAPipeline {

    /** All processors that will run tests against this pipeline */
    private final Set<AttributeProcessor> processors;

    /**
     * Construct the pipeline with all specific processors
     */
    public QAPipeline(){
        // TODO: Move processors initialization App so they can be chosen there
        processors = new HashSet<>();
        processors.add(new ContextualProcessor());
        processors.add(new IntrinsicProcessor());
        processors.add(new RepresentationProcessor());
        // Add new processor here

    }

    /**
     * Run a given sbom against all processor tests within this pipeline
     *
     * @param sbom SBOM to run tests against
     * @return QualityReport containing all results
     */
    public QualityReport process(SBOM sbom){
         // Init QualityReport
         QualityReport qr = new QualityReport(sbom.getSerialNumber());

         // Run all added processors
         for (AttributeProcessor p : processors) {
             // Add results to master report object
             qr.append(p.process(sbom));
         }

         // Return Master QR
         return qr;
    }
}
