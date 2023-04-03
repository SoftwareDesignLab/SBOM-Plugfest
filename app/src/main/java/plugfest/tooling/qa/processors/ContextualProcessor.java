package plugfest.tooling.qa.processors;

public class ContextualProcessor extends AttributeProcessor {
    public ContextualProcessor() {
        super(new MetricTest[]{
                new CompletenessTest(),
                new AppropriateAmountTest(),
                new RelevancyTest(),
                new TimelinessTest()
                // Add new tests here
        });
    }
}
