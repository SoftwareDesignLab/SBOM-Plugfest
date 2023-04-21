package org.nvip.plugfest.tooling.qa.processors;

import org.nvip.plugfest.tooling.qa.test_results.TestResults;
import org.nvip.plugfest.tooling.sbom.Component;

/**
 * Abstract class to be extended by all metric tests
 *
 * @author Dylan Mulligan
 */
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
