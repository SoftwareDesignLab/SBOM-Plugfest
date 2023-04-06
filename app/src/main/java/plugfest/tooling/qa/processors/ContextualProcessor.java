package plugfest.tooling.qa.processors;

/**
 * Metrics that measure the relevance of data in the context of the SBOM
 */
public class ContextualProcessor extends AttributeProcessor {
    public ContextualProcessor() {
        super(new MetricTest[]{
                new CompletenessTest(),
                new AppropriateAmountTest(),
                new TimelinessTest()
                // Add new tests here
        });
    }
}
