package plugfest.tooling.qa.processors;

import plugfest.tooling.sbom.Component;

import java.util.ArrayList;

public abstract class MetricTest {
    //#region Attributes

    private final String name;
    private final ArrayList<String> testResults;

    //#endregion

    //#region Constructors

    protected MetricTest(String name) {
        this.name = name;
        this.testResults = new ArrayList<>();
    }

    //#endregion

    //#region Abstract Methods

    public abstract ArrayList<String> test(Component c);

    //#endregion

    //#region Getters

    public String getName() { return this.name; }
    public ArrayList<String> getTestResults() { return this.testResults; }

    //#endregion

    //#region Setters

    public void addTestResults(ArrayList<String> result) { testResults.addAll(result); }

    //#endregion

}
