package org.nvip.plugfest.tooling.qa.processors;

/**
 * Metrics that measure the relevance of data in the context of the SBOM
 */
public class ContextualProcessor extends AttributeProcessor {
    /**
     * Construct the processor and add all contextual tests to the list of tests to perform
     */
    public ContextualProcessor() {
        super(new MetricTest[]{
                new CompletenessTest(),
                new AppropriateAmountTest(),
                new DataVerificationTest(),
                new ActionableTest()
                // Add new tests here
        });
    }
}
