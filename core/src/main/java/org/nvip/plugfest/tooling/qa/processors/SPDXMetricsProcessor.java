package org.nvip.plugfest.tooling.qa.processors;


import org.nvip.plugfest.tooling.qa.tests.HasDataLicenseSPDXTest;

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

        this.metricTests.add(new HasDataLicenseSPDXTest());

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
