package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.tests.HasMetadataTest;

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
        this.metricTests.add(new HasMetadataTest());
        /*
        TODO
         emptyOrNull: check if the field is empty or null
         minElementFields: Check if the field is empty or null
         validPURL: PURL matches regex
         validCPE: CPE matches regex
         validSWID: SWID matches regex
         hasSupplier: check author details
         hasGenerationDetails: see if there are details on the tool that generated report
         */
    }
}
