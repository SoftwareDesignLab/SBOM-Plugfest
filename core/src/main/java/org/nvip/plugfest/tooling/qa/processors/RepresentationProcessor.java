package org.nvip.plugfest.tooling.qa.processors;

/**
 * Metrics that will assess how well the data in the SBOM represents the SBOM as a whole
 */
public class RepresentationProcessor extends AttributeProcessor {
    public RepresentationProcessor() {
        super(new MetricTest[]{
                // Add new tests here
        });
    }
}
