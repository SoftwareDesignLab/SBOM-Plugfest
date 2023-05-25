package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.test_results.Result;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.util.List;

public abstract class MetricTest {

    // todo maybe remove?
    // utility to check if object is empty or null
    protected boolean isEmptyOrNull(Object o){
        if(o == null)
            return true;

        if(o instanceof String)
            return o.equals("");

        return false;
    }

    public abstract List<Result> test(SBOM sbom);
}
