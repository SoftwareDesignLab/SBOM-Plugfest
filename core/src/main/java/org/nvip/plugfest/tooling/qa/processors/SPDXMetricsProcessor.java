package org.nvip.plugfest.tooling.qa.processors;


/**
 * file: SPDXMetricsProcessor.java
 *
 * Collection of tests to ensure SPDX SBOM specific metrics are included
 * and accurate
 * @author Matthew Morrison
 */
public class SPDXMetricsProcessor extends AttributeProcessor{

    /**
     * Create a new preset collection of tests
     */
    public SPDXMetricsProcessor(){
        this.attributeName = "SPDXMetrics";


        /*
        TODO
           hasBomVersion
           hasDataLicense
           hasSPDXID
           hasDocumentName
           hasDocumentNamespace
           validCreationInfo
           hasDownloadLocation
           hasFilesAnalyzed
           hasVerificationCode
           hasExtractedLicenses
           hasExtractedLicensesDescriptions
           hasExtractedLicensesRef
         */
    }
}
