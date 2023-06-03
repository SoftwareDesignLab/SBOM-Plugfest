package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.tests.*;

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
       this.metricTests.add(new MinElementTest());
       this.metricTests.add(new ValidPurlTest());
       this.metricTests.add(new ValidCPETest());
       //TODO
       //this.metricTests.add(new ValidSWIDTest());
       //this.metricTests.add(new HasRelationshipsTest());
   }
}
