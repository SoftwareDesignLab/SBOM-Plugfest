package plugfest.tooling.qa.processors;

import plugfest.tooling.qa.test_results.TestResults;
import plugfest.tooling.sbom.Component;

public abstract class MetricTest {
    //#region Attributes

    private final String name;

    //#endregion

    //#region Constructors

    protected MetricTest(String name) {
        this.name = name;
    }

    //#endregion

    //#region Abstract Methods

    public abstract TestResults test(Component c);

    //#endregion

    //#region Getters

    public String getName() { return this.name; }

    //#endregion

}