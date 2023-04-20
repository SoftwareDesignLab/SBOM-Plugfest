package org.nvip.plugfest.tooling.qa.processors;

/**
 * Metrics that relate specifically to the data stored in the SBOM
 *
 * @author Dylan Mulligan
 */
public class IntrinsicProcessor extends AttributeProcessor {
    /**
     * Construct the intrinsic processor and add all tests that relate to intrinsic metrics
     */
    public IntrinsicProcessor() {
        super(new MetricTest[]{
                // Add new tests here
        });
    }
}
