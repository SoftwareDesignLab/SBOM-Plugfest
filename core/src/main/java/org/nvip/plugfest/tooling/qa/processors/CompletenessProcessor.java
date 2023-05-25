package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.processors.tests.EmptyOrNullTest;
import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.Signature;

import java.util.ArrayList;
import java.util.List;

public class CompletenessProcessor extends AttributeProcessor {
    private final MetricTest emptyOrNullTest = new EmptyOrNullTest();
   public CompletenessProcessor(){
       this.attributeName = "Completeness";
       this.metricTests.add(new EmptyOrNullTest());
   }
    /*
    emptyOrNull
minElementFields
validPurl
validCPE
validSWID

hasSupplierName
hasVersionName
hasRealtionships
     */


}
