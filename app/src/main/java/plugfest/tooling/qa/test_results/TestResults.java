package plugfest.tooling.qa.test_results;

import plugfest.tooling.sbom.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;

/**
 * File: TestResults.java
 * A class to store organized and formatted test results for an individual component.
 *
 * @author Ian Dunn
 */
public class TestResults {
    /**
     * Component that the test results belong to
     */
    private final Component component;
    private final ArrayList<Test> tests;

    public TestResults(Component component) {
        this.component = component;
        this.tests = new ArrayList<>();
    }

    public Component getComponent() {
        return this.component;
    }

    public ArrayList<Test> getTests() {
        return this.tests;
    }

    public void addTest(Test t) {
        tests.add(t);
    }

    public void addTests(TestResults r) {
        tests.addAll(r.getTests());
    }

    public int getSuccessfulTests() {
        int success = 0;
        for(Test t : tests) {
            if(t.getStatus()) {
                success++;
            }
        }

        return success; // Automatic type casting for the win
    }

    public String finalStatus() {
        return getSuccessfulTests() == tests.size() ? "PASSED" : "FAILED";
    }

    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("Component %s %s with %d/%d Tests Passed:\n",
                component.getName(), finalStatus(), getSuccessfulTests(), tests.size()));

        for(Test t : tests) {
            out.append(String.format("  %s\n", t.toString()));
        }
        return out.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TestResults results = (TestResults) o;

        if (!component.equals(results.getComponent())) return false;
        return tests.equals(results.getTests());
    }
}
