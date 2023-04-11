package org.nvip.plugfest.tooling.qa;

import org.nvip.plugfest.tooling.qa.processors.AttributeProcessor;
import org.nvip.plugfest.tooling.qa.processors.ContextualProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.HashSet;
import java.util.Set;

public class QAPipeline {

    private final Set<AttributeProcessor> processors;

    public QAPipeline(){
        // TODO: Move processors initialization App so they can be chosen there
        processors = new HashSet<>();
        processors.add(new ContextualProcessor());
        // TODO: figure out how to return TestResults from SPDXProcesor the same way as ContextualProcessor
//        processors.add(new SPDXProcessor());
        // Add new processor here
    }

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
