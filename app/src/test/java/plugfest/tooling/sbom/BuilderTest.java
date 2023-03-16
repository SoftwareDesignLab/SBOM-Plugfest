package plugfest.tooling.sbom;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class BuilderTest {

    /**
     * Test Constants
     */

    private static final String TEST_SPDX_v2_3_SBOM = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine.2-3.spdx";

    private static final String TEST_SBOM_DOESNT_EXIST = "src/test/java/plugfest/tooling/sample_boms/sbom.idontexist.spdx";

    private static final String TEST_SBOM_SPDX_NO_COMPONENTS = "src/test/java/plugfest/tooling/sample_boms/sbom.nocomponents.2-3.spdx";

    private static final String TEST_SPDX_v2_2_SBOM = "src/test/java/plugfest/tooling/sample_boms/sbom.docker.2-2.spdx";

    private static final String TEST_SPDX_LARGE_v2_3_SBOM = "src/test/java/plugfest/tooling/sample_boms/sbom.pythom.2-3.spdx";



    @Test
    public void builder_makes_SBOM_test() throws IOException {
        SBOM test = Builder.builder(TEST_SPDX_v2_3_SBOM);
        assertNotNull(test);
    }

    @Test
    public void builder_does_not_make_SBOM_from_blank_path() throws IOException {
        SBOM test = Builder.builder("");
        assertNull(test);
    }

    @Test
    public void builder_does_not_make_SBOM_from_non_existing_file() throws IOException {
        SBOM test = Builder.builder(TEST_SBOM_DOESNT_EXIST);
        assertNull(test);
    }

    @Test
    public void builder_gets_all_raw_data_from_SBOM_correctly_test() throws IOException {
        SBOM test = Builder.builder(TEST_SPDX_v2_3_SBOM);
        String current_line;
        BufferedReader br = new BufferedReader(new FileReader(TEST_SPDX_v2_3_SBOM));
        for (String data : test.data) {
            current_line = br.readLine();
            assertEquals(current_line, data);
        }
    }

    @Test
    public void builder_makes_SBOM_from_SPDX_2_3_test() throws IOException {
        SBOM test = Builder.builder(TEST_SPDX_v2_2_SBOM);
        assertNotNull(test);
    }

    @Test
    public void builder_gets_all_raw_data_from_SPDX_2_3_SBOM_correctly_test() throws IOException {
        SBOM test = Builder.builder(TEST_SPDX_v2_2_SBOM);
        String current_line;
        BufferedReader br = new BufferedReader(new FileReader(TEST_SPDX_v2_2_SBOM));
        for (String data : test.data) {
            current_line = br.readLine();
            assertEquals(current_line, data);
        }
    }
}
