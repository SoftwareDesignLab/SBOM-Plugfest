package plugfest.tooling.qa.processors;

import plugfest.tooling.sbom.Component;

public abstract class MetricTest {
    //#region Attributes

    private final String name;
    private final StringBuilder testResults;

    //#endregion

    //#region Constructors

    protected MetricTest(String name) {
        this.name = name;
        this.testResults = new StringBuilder();
    }

    //#endregion

    //#region Abstract Methods

    public abstract String test(Component c);

    //#endregion

    //#region Getters

    public String getName() { return this.name; }
    public String getTestResults() { return this.testResults.toString(); }

    //#endregion

    //#region Setters

    public void addTestResult(String result) { testResults.append(result); }

    //#endregion

}
