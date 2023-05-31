/**
 * @file TranslatorCDXTest.java
 *
 * Test set for TranslatorCDX class
 *
 * @author Tyler Drake
 */

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

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

/**
 * File: TranslatorCDXXMLTest.java
 * Tests for TranslatorCDXXML
 *
 * @author Tyler Drake
 */
public class TranslatorCDXXMLTest extends TranslatorTestCore<TranslatorCDXXML> {

    public static final String test_small_cdx = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine.xml";
    public static final String test_large_cdx = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.python.xml";
    public static final String test_no_metadata_cdx = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.nometadata.xml";
    public static final String test_no_components_cdx = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.nocomponents.xml";
    public static final String TEST_CDX_SBOM_1_2_DEPENDENCIES = "src/test/java/org/nvip/plugfest/tooling/sample_boms/proton-bridge-v1.8.0.bom.xml";

    public static final String TEST_CDX_SBOM_1_4_DEPENDENCIES = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.cdxgen.1-4.xml";


    protected TranslatorCDXXMLTest() {
        super(new TranslatorCDXXML());
    }


    @ParameterizedTest
    @ValueSource(strings = { test_small_cdx, test_no_metadata_cdx })
    public void translatorxml_small_file_test(String pathToSBOM) throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(pathToSBOM);
        assertNotNull(sbom);
        Assertions.assertEquals(SBOM.Type.CYCLONE_DX, sbom.getOriginFormat());
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("http://cyclonedx.org/schema/bom/1.4", sbom.getSpecVersion());
        assertEquals(18, sbom.getAllComponents().size());
    }

    @Test
    public void translatorxml_large_file_test() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(test_large_cdx.toString());
        assertNotNull(sbom);
        assertEquals(SBOM.Type.CYCLONE_DX, sbom.getOriginFormat());
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("http://cyclonedx.org/schema/bom/1.4", sbom.getSpecVersion());
        assertEquals(434, sbom.getAllComponents().size());
    }

    @Test
    public void translatorxml_no_components_test() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(test_no_components_cdx.toString());
        assertNotNull(sbom);
        // Should be 1 component for head component
        assertEquals(SBOM.Type.CYCLONE_DX, sbom.getOriginFormat());
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("http://cyclonedx.org/schema/bom/1.4", sbom.getSpecVersion());
        assertEquals(1, sbom.getAllComponents().size());
    }

    @Test
    public void translatorxml_v1_2_dependencies_test() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_CDX_SBOM_1_2_DEPENDENCIES.toString());
        assertNotNull(sbom);
        assertEquals(202, sbom.getAllComponents().size());
    }

    @Test
    public void translatorxml_v1_2_dependencies_other_test() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(TEST_CDX_SBOM_1_4_DEPENDENCIES);
        assertNotNull(sbom);
        assertEquals(631, sbom.getAllComponents().size());
    }

    @Test
    @DisplayName("Test for null UIDs")
    void nullUIDTest() throws TranslatorException {
        SBOM sbom = this.TRANSLATOR.translate(test_large_cdx);

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
