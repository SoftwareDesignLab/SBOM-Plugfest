package org.nvip.plugfest.tooling.integration;

import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.differ.Comparer;
import org.nvip.plugfest.tooling.differ.DiffReport;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.translator.TranslatorSPDX;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class PlugFestIntegrationTest {

    private static final String TEST_SPDX_v2_3_SBOM = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine.2-3.spdx";

    private static final String TEST_SPDX_v2_3_DIFF_BOM = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine-compare.2-3.spdx";

    private static final String TEST_CDX_SBOM = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine.xml";

    private static final String TEST_CDX_DIFF_SBOM = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine-compare.xml";

    @Test
    public void full_diff_report_from_SPDX_SBOM() throws IOException {

        // Create first SBOM
        SBOM test_sbom_one = TranslatorSPDX.translatorSPDX(TEST_SPDX_v2_3_SBOM);
        assertNotNull(test_sbom_one);

        // Create second SBOM
        SBOM test_sbom_two = TranslatorSPDX.translatorSPDX(TEST_SPDX_v2_3_DIFF_BOM);
        assertNotNull(test_sbom_two);

        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);

        assertNotNull(test_report);

    }
    @Test
    public void full_diff_report_from_CDX_SBOM() throws IOException {

        // Create first SBOM
        SBOM test_sbom_one = TranslatorSPDX.translatorSPDX(TEST_CDX_SBOM);
        assertNotNull(test_sbom_one);

        // Create second SBOM
        SBOM test_sbom_two = TranslatorSPDX.translatorSPDX(TEST_CDX_DIFF_SBOM);
        assertNotNull(test_sbom_two);

        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);

        assertNotNull(test_report);

    }

}
