/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package plugfest.tooling;

import plugfest.tooling.metrics.*;

public class App {
    public String getGreeting() {
        return "Hello World!";
    }

    public static void main(String[] args) {
        System.out.println(new App().getGreeting());

        //System.out.println("\nMetrics Test\n");
        // Creates New Metrics Object
        //Metrics metrics = new Metrics(" PATH TO SPDX FILES ");
        // COMPARISON METHOD TEST
        //String[] sbom_args = {"example.sbom.alpine.spdx", "example.sbom.python.spdx"};
        //metrics.compare(sbom_args);
        // VERIFY METHOD TEST
        //metrics.verify("example.sbom.python.spdx");
    }
}
