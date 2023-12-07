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

import org.cyclonedx.exception.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.nvip.plugfest.tooling.Debug;
import org.nvip.plugfest.tooling.sbom.Component;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.SBOM.Type;
import org.nvip.plugfest.tooling.translator.TranslatorSPDX;
import static org.nvip.plugfest.tooling.translator.utils.Utils.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

/**
 * File: SPDXParser.java
 * Tests for SPDXParser class
 *
 * @author Tyler Drake
 */
public class TranslatorSPDXTest extends TranslatorTestCore<TranslatorSPDX> {

    /**
     * Test Constants
     */

    private static final String TEST_SPDX_v2_3_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine.2-3.spdx";
    private static final String TEST_SPDX_v2_3_SBOM_NOMETADATA = "src/test/java/org/nvip/plugfest/tooling/sample_boms" +
            "/sbom.alpine.2-3.nometadata.spdx";

    private static final String TEST_SPDX_v2_3_SBOM_LICENSINGINFO = "src/test/java/org/nvip/plugfest/tooling" +
            "/sample_boms/sbom.alpine.2-3.licensinginfo.spdx";

    private static final String TEST_SPDX_v2_2_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.docker.2-2.spdx";

    private static final String TEST_SPDX_LARGE_v2_3_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.python.2-3.spdx";

    private static final String TEST_SBOM_DOESNT_EXIST = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.idontexist.spdx";

    private static final String TEST_SBOM_SPDX_NO_COMPONENTS = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.nocomponents.2-3.spdx";

    private static final String TEST_SBOM_SPDX_EMPTY = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.empty.2-3.spdx";

    public TranslatorSPDXTest() { super(new TranslatorSPDX()); }

    /**
     * Tests
     */

    @ParameterizedTest
    @ValueSource(strings = { TEST_SPDX_v2_3_SBOM, TEST_SPDX_v2_3_SBOM_NOMETADATA })
    public void builder_makes_SBOM_test(String pathToSBOM) throws TranslatorException {
        SBOM test = this.TRANSLATOR.translate(pathToSBOM);
        assertNotNull(test);
        Assertions.assertEquals(SBOM.Type.SPDX, test.getOriginFormat());
        assertEquals("1", test.getSbomVersion());
        assertEquals("SPDX-2.3", test.getSpecVersion());
        assertEquals(17, test.getAllComponents().size());
    }

    @Test
    public void builder_makes_SBOM_from_SPDX_2_2_test() throws TranslatorException {
        SBOM test = this.TRANSLATOR.translate(TEST_SPDX_v2_2_SBOM);
        assertNotNull(test);
        assertEquals(SBOM.Type.SPDX, test.getOriginFormat());
        assertEquals("1", test.getSbomVersion());
        assertEquals("SPDX-2.2", test.getSpecVersion());
        assertEquals(138, test.getAllComponents().size());
        assertTrue(noLicensesCheck(test));
    }


    @Test
    public void builder_makes_large_SBOM_test() throws TranslatorException {
        SBOM test = this.TRANSLATOR.translate(TEST_SPDX_LARGE_v2_3_SBOM);
        assertNotNull(test);
        assertEquals(SBOM.Type.SPDX, test.getOriginFormat());
        assertEquals("1", test.getSbomVersion());
        assertEquals("SPDX-2.3", test.getSpecVersion());
        assertEquals(433, test.getAllComponents().size());
    }


    @Test
    public void builder_does_not_make_SBOM_from_blank_path() throws TranslatorException {
        SBOM test = this.TRANSLATOR.translate("");
        assertNull(test);
    }

    @Test
    public void builder_does_not_make_SBOM_from_non_existing_file() throws TranslatorException {
        SBOM test = this.TRANSLATOR.translate(TEST_SBOM_DOESNT_EXIST);
        assertNull(test);
    }

    @Test
    public void builder_parses_SBOM_with_no_components() throws TranslatorException {
        SBOM test = this.TRANSLATOR.translate(TEST_SBOM_SPDX_NO_COMPONENTS);
        assertNotNull(test);
        assertEquals(SBOM.Type.SPDX, test.getOriginFormat());
        assertEquals("1", test.getSbomVersion());
        assertEquals("SPDX-2.3", test.getSpecVersion());
        assertEquals(1, test.getAllComponents().size());
    }

    @Test
    public void builder_parses_SBOM_that_is_empty() throws TranslatorException {
        SBOM test = this.TRANSLATOR.translate(TEST_SBOM_SPDX_EMPTY);
        assertNotNull(test);
        assertEquals(SBOM.Type.SPDX, test.getOriginFormat());
        assertEquals("1", test.getSbomVersion());
        assertEquals(null, test.getSpecVersion());
        assertEquals(1, test.getAllComponents().size());
    }

    @Test
    @DisplayName("Single-Line Licensing Information")
    void extractedLicenseOneLineTest() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_SPDX_v2_3_SBOM_LICENSINGINFO);
        for(Component component : sbom.getAllComponents()) {
            if(!component.getName().equals("ssl_client")) continue;

            Set<String> licenses = component.getLicenses();
            assertEquals(1, licenses.size());
            Debug.log(Debug.LOG_TYPE.SUMMARY, "Found licenses in component ssl_client: " + licenses);

            Map<String, Map<String, String>> extractedLicenses = component.getExtractedLicenses();
            assertEquals(1, extractedLicenses.size());
            Debug.log(Debug.LOG_TYPE.SUMMARY,
                    "Found extracted licenses in component ssl_client: " + extractedLicenses);

            Map<String, String> attributes = extractedLicenses.get("LicenseRef-TestLicense-0");
            assertEquals("Test License 0", attributes.get("name"));
            assertEquals("Test license text", attributes.get("text"));
            assertEquals("https://google.com", attributes.get("crossRef"));
        }
    }

    @Test
    @DisplayName("Multi-Line Licensing Information")
    void extractedLicensingMultiLineTest() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_SPDX_v2_3_SBOM_LICENSINGINFO);
        for(Component component : sbom.getAllComponents()) {
            if(!component.getName().equals("musl")) continue;

            Set<String> licenses = component.getLicenses();
            assertEquals(1, licenses.size());
            Debug.log(Debug.LOG_TYPE.SUMMARY, "Found licenses in component musl: " + licenses);

            Map<String, Map<String, String>> extractedLicenses = component.getExtractedLicenses();
            assertEquals(1, extractedLicenses.size());
            Debug.log(Debug.LOG_TYPE.SUMMARY,
                    "Found extracted licenses in component musl: " + extractedLicenses);

            Map<String, String> attributes = extractedLicenses.get("LicenseRef-TestLicense-1");
            assertEquals("Test License 1", attributes.get("name"));
            assertTrue(attributes.get("text").contains("\n"));
            assertEquals("https://bing.com", attributes.get("crossRef"));
        }
    }

    @Test
    @DisplayName("Test for null UIDs")
    void nullUIDTest() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_SPDX_LARGE_v2_3_SBOM);

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
}
