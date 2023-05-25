package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.processors.tests.EmptyOrNullTest;

public class CompletenessProcessor extends AttributeProcessor {
   public CompletenessProcessor(){
       this.attributeName = "Completeness";
       this.metricTests.add(new EmptyOrNullTest());
       /*
        todo
        minElementFields
        validPurl
        validCPE
        validSWID
        hasSupplierName
        hasVersionName
        hasRelationships
        */

   }


}
