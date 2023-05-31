package org.nvip.plugfest.tooling.translator;

import org.cyclonedx.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.nvip.plugfest.tooling.sbom.SBOM;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * File: TranslatorCDXJSONTest.java
 * Tests for TranslatorCDXJSON
 *
 * @author Tyler Drake
 */
public class TranslatorCDXJSONTest extends TranslatorTestCore<TranslatorCDXJSON> {

    public static final String TEST_SMALL_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/sbom.alpine.json";
    public static final String TEST_SMALL_CDX_JSON_NOMETADATA = "src/test/java/org/nvip/plugfest/tooling/sample_boms" +
            "/cdx_json/sbom.alpine.nometadata.json";

    public static final String TEST_MEDIUM_CDX_JSON ="src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/trivy-0.39.0_celery-3.1.cdx.json";

    public static final String TEST_ANOTHER_SMALL_SYFT_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/cdx.json";

    public static final String TEST_SMALL_SYFT_CDX_JSON_HASHES = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/bom.json";


    protected TranslatorCDXJSONTest() {
        super(new TranslatorCDXJSON());
    }

    @ParameterizedTest
    @ValueSource(strings = { TEST_SMALL_CDX_JSON, TEST_SMALL_CDX_JSON_NOMETADATA })
    public void build_SBOM_from_small_cdx_json_test(String pathToSBOM) throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(pathToSBOM);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("1.4", sbom.getSpecVersion());
        assertEquals(17, sbom.getAllComponents().size());
    }

    @Test
    public void build_SBOM_from_medium_cdx_json_test() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_MEDIUM_CDX_JSON);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("1.4", sbom.getSpecVersion());
        assertEquals(124, sbom.getAllComponents().size());
    }

    @Test
    public void build_SBOM_from_another_small_syft_json_test() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_ANOTHER_SMALL_SYFT_CDX_JSON);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("1.4", sbom.getSpecVersion());
        assertEquals(48, sbom.getAllComponents().size());
    }

}
