package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.tests.HasBomRefTest;
import org.nvip.plugfest.tooling.qa.tests.HasBomVersionTest;

/**
 * file: CDXMetricsProcessor.java
 *
 * A collection of tests that are tailored to CycloneDX SBOM specific metrics
 * @author Matthew Morrison
 */
public class CDXMetricsProcessor extends AttributeProcessor{

    /**
     * Create new preset collection of tests
     */
    public CDXMetricsProcessor(){
        this.attributeName = "CDXMetrics";
        this.metricTests.add(new HasBomRefTest());
        this.metricTests.add(new HasBomVersionTest());
    }
}
