package org.nvip.plugfest.tooling.qa.processors;

/**
 * Metrics that will assess how well the data in the SBOM represents the SBOM as a whole
 *
 * @author Dylan Mulligan
 */
public class RepresentationProcessor extends AttributeProcessor {
    /**
     * Construct the representation processor and add all tests that relate to representation metrics
     */
    public RepresentationProcessor() {
        super(new MetricTest[]{
                // Add new tests here
        });
    }
}
