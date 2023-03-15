package plugfest.tooling.sbom;

import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BuilderTest {
    @Test
    public void builder_makes_SBOM_test() throws IOException {
        SBOM test = Builder.builder("src/test/java/plugfest/tooling/sample_boms/sbom.alpine.spdx");
        assertNotNull(test);
    }
}
