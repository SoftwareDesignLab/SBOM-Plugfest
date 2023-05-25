package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.qa.processors.MetricTest;
import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

public abstract class AttributeProcessor {

    private final List<MetricTest> tests;

    protected AttributeProcessor(List<MetricTest> tests) {
        this.tests = tests;
    }

    public List<Result> process(SBOM sbom){
        List<Result> results = new ArrayList<>();
        for(MetricTest test : this.tests)
            results.add(test.test(sbom));

        return results;

    }
}
