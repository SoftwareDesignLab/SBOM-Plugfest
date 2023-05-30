package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.tests.HasHashDataTest;
import org.nvip.plugfest.tooling.qa.tests.ValidHashDataTest;

public class UniquenessProcessor extends AttributeProcessor {

    public UniquenessProcessor(){
        this.attributeName = "Uniqueness";
        this.metricTests.add(new HasHashDataTest());
        this.metricTests.add(new ValidHashDataTest());
        /*
        TODO
         validHashData: Check to see hash matches schema
         accuratePURL: Purl matches stored component data
         accurateCPE: CPE matches stored component data
         accurateSWID: SWID matches stored swid data
         */
    }
}
