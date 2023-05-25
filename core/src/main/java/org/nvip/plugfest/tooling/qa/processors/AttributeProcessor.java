package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.QualityReport;
import org.nvip.plugfest.tooling.qa.processors.MetricTest;
import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.ArrayList;
import java.util.List;

public abstract class AttributeProcessor {

    private final String attributeName;

    protected final List<MetricTest> tests = new ArrayList<>();

    protected AttributeProcessor(String attributeName){
        this.attributeName = attributeName;
    }

    public List<Result> process(SBOM sbom){
        List<Result> results = new ArrayList<>();
        for(MetricTest test : this.tests)
            results.add(test.test(sbom));

        return results;

    }

    public String getAttributeName() {
        return this.attributeName;
    }
}
