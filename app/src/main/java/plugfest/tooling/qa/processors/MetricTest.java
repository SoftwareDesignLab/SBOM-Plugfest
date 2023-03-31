package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.test_results.Test;
import plugfest.tooling.qa.test_results.TestResults;
import plugfest.tooling.sbom.Component;

import java.util.ArrayList;

public abstract class MetricTest {
    //#region Attributes

    private final String name;
    private final TestResults testResults;

    //#endregion

    //#region Constructors

    protected MetricTest(String name) {
        this.name = name;
        this.testResults = null; // TODO: Initialized in test
    }

    //#endregion

    //#region Abstract Methods

    public abstract TestResults test(Component c);

    //#endregion

    //#region Getters

    public String getName() { return this.name; }
    public TestResults getTestResults() { return this.testResults; }

    //#endregion

}
