package plugfest.tooling.integration;

import org.junit.jupiter.api.Test;
import plugfest.tooling.sbom.SBOM;
import plugfest.tooling.sbom.TranslatorSPDX;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlugFestIntegrationTest {

    private static final String TEST_SPDX_v2_3_SBOM = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine.2-3.spdx";

    private static final String TEST_SPDX_v2_3_DIFF_BOM = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine-compare.2-3.spdx";

    private static final String EXPECTED_CONSOLE_PRINT =
            "<< sbom.alpine-compare.2-3.spdx : > sbom.alpine.2-3.spdx\n" +
            "-----\n" +
            "4\n" +
            "=====\n" +
            "< DocumentName: alpine\n" +
            "> DocumentName: alpine:latest\n" +
            "\n" +
            "9\n" +
            "=====\n" +
            "< Created: 2023-03-19T22:38:07Z\n" +
            "> Created: 2023-03-10T14:43:10Z\n" +
            "\n" +
            ">";


    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @Test
    public void full_diff_report_from_SPDX_SBOM() throws IOException {

        // Create first SBOM
        SBOM test_sbom_one = TranslatorSPDX.translatorSPDX(TEST_SPDX_v2_3_SBOM);
        assertNotNull(test_sbom_one);

        // Create second SBOM
        SBOM test_sbom_two = TranslatorSPDX.translatorSPDX(TEST_SPDX_v2_3_DIFF_BOM);
        assertNotNull(test_sbom_two);

    }

}
