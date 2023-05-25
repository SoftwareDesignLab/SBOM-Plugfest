package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.processors.tests.EmptyOrNullTest;

public class CompletenessProcessor extends AttributeProcessor {
   public CompletenessProcessor(String name){
       super(name);
       this.tests.add(new EmptyOrNullTest());
   }
}
