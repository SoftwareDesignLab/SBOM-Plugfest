package org.nvip.plugfest.tooling.translator;

import org.cyclonedx.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * File: TranslatorCDXJSONTest.java
 * Tests for TranslatorCDXJSON
 *
 * @author
 */
public class TranslatorCDXJSONTest {

    public static final String TEST_SMALL_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/sbom.alpine.json";

    public static final String TEST_MEDIUM_CDX_JSON ="src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/trivy-0.39.0_celery-3.1.cdx.json";

    public static final String TEST_ANOTHER_SMALL_SYFT_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/cdx.json";

    @Test
    public void build_SBOM_from_small_cdx_json_test() throws IOException, ParseException {
        SBOM sbom = TranslatorCDXJSON.translatorCDXJSON(TEST_SMALL_CDX_JSON);
        assertNotNull(sbom);
    }

    @Test
    public void build_SBOM_from_medium_cdx_json_test() throws IOException, ParseException {
        SBOM sbom = TranslatorCDXJSON.translatorCDXJSON(TEST_MEDIUM_CDX_JSON);
        assertNotNull(sbom);
    }

    @Test
    public void build_SBOM_from_another_small_syft_json_test() throws IOException, ParseException {
        SBOM sbom = TranslatorCDXJSON.translatorCDXJSON(TEST_ANOTHER_SMALL_SYFT_CDX_JSON);
        assertNotNull(sbom);
    }

}
