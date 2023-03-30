package plugfest.tooling.qa.processors;

public class ContextualProcessor extends AttributeProcessor {
    public ContextualProcessor() {
        super(new MetricTest[]{
                new CompletenessTest()
                // Add new tests here
        });
    }
}
