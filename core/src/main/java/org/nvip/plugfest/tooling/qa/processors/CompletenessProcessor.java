package org.nvip.plugfest.tooling.qa.processors;

/**
 * file: CompletenessProcessor.java
 *
 * Collection of tests to ensure the SBOM's data fields are complete
 * @author Derek Garcia
 */
public class CompletenessProcessor extends AttributeProcessor{

    /**
     * Create new preset collection of tests
     */
    public CompletenessProcessor(){
       this.attributeName = "Completeness";
       /*
        todo
        minElementFields - Check SBOM for min elements
        validPurl - Check if purl string is correct
        validCPE - Check if CPE string is correct
        validSWID - Check if SWID string is correct
        hasRelationships - Check to see if a dependency tree was built
        */
   }
}
