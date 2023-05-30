package org.nvip.plugfest.tooling.qa.processors;

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
        /*
        TODO
         hasMetadata: Check to see if metadata is preseent
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
