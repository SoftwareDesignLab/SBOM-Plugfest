package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

public abstract class AttributeProcessor {

    protected List<MetricTest> metricTests = new ArrayList<>();
    protected String attributeName;

    // run all processors
    public List<Result> process(SBOM sbom){
        List<Result> results = new ArrayList<>();

        for(MetricTest test : this.metricTests){
            results.addAll(test.test(sbom));
        }
        return results;
    }

    public String getAttributeName() {
        return this.attributeName;
    }
}
