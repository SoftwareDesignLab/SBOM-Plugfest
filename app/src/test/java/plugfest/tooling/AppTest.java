/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package plugfest.tooling;


import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppTest {
    @Test
    public void appHasAGreeting() throws IOException {
        App classUnderTest = new App();
        String[] args = {"-m", "src/test/java/plugfest/tooling/sample_boms/sbom.alpine.2-3.spdx"};
        App.main(args);
        assertNotNull(classUnderTest);
    }
}
