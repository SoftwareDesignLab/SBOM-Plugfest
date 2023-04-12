package org.nvip.plugfest.tooling.qa.processors;

/**
 * Metrics that measure the relevance of data in the context of the SBOM
 */
public class ContextualProcessor extends AttributeProcessor {
    public ContextualProcessor() {
        super(new MetricTest[]{
                new CompletenessTest(),
                new AppropriateAmountTest(),
                new DataVerificationTest()
                // Add new tests here
        });
    }
}