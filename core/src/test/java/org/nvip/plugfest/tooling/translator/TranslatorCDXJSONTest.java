package org.nvip.plugfest.tooling.translator;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;
import static org.nvip.plugfest.tooling.translator.utils.Utils.*;


import static org.junit.jupiter.api.Assertions.*;

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

    public static final String TEST_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/cdxgen-8.4.6-source.json";

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
        assertEquals(17, sbom.getAllComponents().size()); // TODO ensure no duplicates added?

        if(pathToSBOM.equals(TEST_SMALL_CDX_JSON)) {
            assertEquals(1, sbom.getMetadata().size());
            assertEquals(2, sbom.getAppTools().size());
        }
        else{
            assertEquals(0, sbom.getMetadata().size());
            assertEquals(1, sbom.getAppTools().size());
        }
        assertFalse(toolsInMetadata(sbom));
    }

    @Test
    public void build_SBOM_from_medium_cdx_json_test() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_MEDIUM_CDX_JSON);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("1.4", sbom.getSpecVersion());
        assertEquals(124, sbom.getAllComponents().size());
        assertEquals(1, sbom.getMetadata().size());
        assertEquals(1, sbom.getAppTools().size());
        assertFalse(toolsInMetadata(sbom));

    }

    @Test
    public void build_SBOM_from_another_small_syft_json_test() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_ANOTHER_SMALL_SYFT_CDX_JSON);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("1.4", sbom.getSpecVersion());
        assertEquals(48, sbom.getAllComponents().size());
        assertEquals(1, sbom.getMetadata().size());
        assertEquals(1, sbom.getAppTools().size());
        assertFalse(toolsInMetadata(sbom));
    }

    @Test
    @DisplayName("Test for null UIDs")
    void nullUIDTest() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_MEDIUM_CDX_JSON);

        for (Component component : sbom.getAllComponents()) {
            Debug.logBlockTitle(component.getName());

            assertFalse(component.getCpes().contains(null));
            Debug.log(Debug.LOG_TYPE.SUMMARY, "Component does not contain null CPEs");

            assertFalse(component.getPurls().contains(null));
            Debug.log(Debug.LOG_TYPE.SUMMARY, "Component does not contain null PURLs");

            assertFalse(component.getSwids().contains(null));
            Debug.log(Debug.LOG_TYPE.SUMMARY, "Component does not contain null SWIDs");

            Debug.logBlock();
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {TEST_CDX_JSON})
    @DisplayName("Test on PlugFest Audit excel line 24")
    public void build_SBOM_cdx_json_test(String pathToSBOM) throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(pathToSBOM);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("1.4", sbom.getSpecVersion());
        assertEquals(5, sbom.getAllComponents().size());
    }

}
