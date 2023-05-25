package org.nvip.plugfest.tooling.qa;

import org.nvip.plugfest.tooling.qa.processors.AttributeProcessor;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.Set;

/**
 * Pipeline that will run all tests against a given SBOM
 *
 * @author Dylan Mulligan
 * @author Matt London
 * @author Derek Garcia
 */
public class QAPipeline {

    /**
     * Run a given sbom against all processor tests within this pipeline
     *
     * @param uid Unique filename used to ID the SBOM
     * @param sbom SBOM to run tests against
     * @param processors Collection of Processors to run against SBOM
     * @return QualityReport containing all results
     */
    public static QualityReport process(String uid, SBOM sbom, Set<AttributeProcessor> processors){
         // Init QualityReport
         QualityReport qr = new QualityReport(uid);

         // Run all added processors
         for (AttributeProcessor p : processors)
             qr.updateAttribute(p.getAttributeName(), p.process(sbom));

         // Return Master QR
         return qr;
    }
}
