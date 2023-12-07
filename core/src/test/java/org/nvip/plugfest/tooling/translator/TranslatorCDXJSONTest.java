/ **
* Copyright 2021 Rochester Institute of Technology (RIT). Developed with
* government support under contract 70RCSA22C00000008 awarded by the United
* States Department of Homeland Security for Cybersecurity and Infrastructure Security Agency.
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the “Software”), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
* SOFTWARE.
* /

package org.nvip.plugfest.tooling.translator;

import org.junit.jupiter.api.Disabled;
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
    public static final String TEST_CDX_JSON_NO_LICENSES = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/CDXMavenPlugin_build_cdx.json";

    protected TranslatorCDXJSONTest() {
        super(new TranslatorCDXJSON());
    }

    @Test
    public void noLicenseTest() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_CDX_JSON_NO_LICENSES);
        assertNotNull(sbom);

        assertTrue(noLicensesCheck(sbom));

        assertEquals("1", sbom.getSbomVersion());
        assertEquals("1.4", sbom.getSpecVersion());
        assertEquals(17, sbom.getAllComponents().size());
        assertEquals(1, sbom.getMetadata().size());
        assertEquals(1, sbom.getAppTools().size());
        assertFalse(checkForAppTools(sbom));
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
        assertFalse(checkForAppTools(sbom));
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
        assertFalse(checkForAppTools(sbom));

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
        assertFalse(checkForAppTools(sbom));
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
   // @DisplayName("Test on PlugFest Audit excel line 24") // todo
    @DisplayName("FIX")
    @Disabled
    public void build_SBOM_cdx_json_test(String pathToSBOM) throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(pathToSBOM);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("1.4", sbom.getSpecVersion());
        assertEquals(5, sbom.getAllComponents().size());
    }

}
