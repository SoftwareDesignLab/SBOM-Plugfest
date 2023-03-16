package plugfest.tooling.sbom;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BuilderTest {
    @Test
    public void builder_makes_SBOM_test() throws IOException {
        SBOM test = Builder.builder("src/test/java/plugfest/tooling/sample_boms/sbom.alpine.spdx");
        assertNotNull(test);
    }

    @Test
    public void builder_does_not_make_SBOM_from_blank_path() throws IOException {
        SBOM test = Builder.builder("");
        assertNull(test);
    }

    @Test
    public void builder_does_not_make_SBOM_from_non_existing_file() throws IOException {
        SBOM test = Builder.builder("src/test/java/plugfest/tooling/sample_boms/sbom.idontexist.spdx");
        assertNull(test);
    }

    @Test
    public void builder_gets_all_raw_data_from_SBOM_correctly_test() throws IOException {
        SBOM test = Builder.builder("src/test/java/plugfest/tooling/sample_boms/sbom.alpine.spdx");
        String current_line;
        BufferedReader br = new BufferedReader(new FileReader("src/test/java/plugfest/tooling/sample_boms/sbom.alpine.spdx"));
        for (String data : test.data) {
            current_line = br.readLine();
            assertEquals(current_line, data);
        }
    }
}
