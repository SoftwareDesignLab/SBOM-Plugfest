package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.tests.HasMinElsMetaDataTest;

/**
 * file: MetadataProcessor.java
 *
 * Collection of tests to ensure that an SBOM's Metadata is present and
 * contains relevant information
 * @author Matthew Morrison
 */
public class MetadataProcessor extends AttributeProcessor{

    /**
     * Create a new preset collection of SBOM Metadata tests
     */
    public MetadataProcessor(){
        this.attributeName = "Metadata";

        this.metricTests.add(new HasMinElsMetaDataTest());

        /*
        TODO
         hasMetaData: check if the sbom contains metadata
         validPURL: PURL matches regex
         validCPE: CPE matches regex
         validSWID: SWID matches regex
         hasGenerationDetails: see if there are details on the tool that generated report
         */
    }
}
