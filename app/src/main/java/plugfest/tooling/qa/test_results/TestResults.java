package plugfest.tooling.qa.test_results;

import plugfest.tooling.sbom.Component;

import java.util.ArrayList;

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

    /**
     * Initialize a new instance of TestResults
     * @param component The component that the Tests belong to
     */
    public TestResults(Component component) {
        this.component = component;
        this.tests = new ArrayList<>();
    }

    /**
     * Get the component that the tests belong to.
     *
     * @return The component that the tests belong to
     */
    public Component getComponent() {
        return this.component;
    }

    /**
     * Get a list of all tests, regardless of passing or failing.
     *
     * @return An ArrayList of all tests performed
     */
    public ArrayList<Test> getTests() {
        return this.tests;
    }

    /**
     * Add a single test
     *
     * @param t Test to add
     */
    public void addTest(Test t) {
        tests.add(t);
    }

    /**
     * Add multiple tests from another TestResults instance
     *
     * @param r The TestResults to add to this instance
     */
    public void addTests(TestResults r) {
        tests.addAll(r.getTests());
    }

    /**
     * Get the amount of successful tests ran on the component.
     *
     * @return The number of successful tests ran on the component
     */
    public int getSuccessfulTests() {
        int success = 0;
        for(Test t : tests) {
            if(t.getStatus()) {
                success++;
            }
        }

        return success; // Automatic type casting for the win
    }

    /**
     * Get the final status of the component based on all tests. This currently only marks the component as passed if
     *  all its tests are passed.
     *
     * @return "PASSED" if the component passed, "FAILED" otherwise
     */
    public String finalStatus() {
        return getSuccessfulTests() == tests.size() ? "PASSED" : "FAILED";
    }

    /**
     * Prints the component name, final status, and number of tests passed vs total tests.
     *  Underneath the header is printed the status and message of each individual Test.
     *
     * @return A String representation of all test results.
     */
    @Override
    public String toString() {
        StringBuilder out = new StringBuilder();
        out.append(String.format("Component '%s' %s with %d/%d Tests Passed:\n",
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
