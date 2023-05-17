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
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.sbom.SBOMType;

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.parsers.ParserConfigurationException;

/**
 * File: TranslatorCDXXMLTest.java
 * Tests for TranslatorCDXXML
 *
 * @author Tyler Drake
 */
public class TranslatorCDXXMLTest {

    public static final String test_small_cdx = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine.xml";
    public static final String test_large_cdx = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.python.xml";
    public static final String test_no_metadata_cdx = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.nometadata.xml";
    public static final String test_no_components_cdx = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.nocomponents.xml";
    public static final String TEST_CDX_SBOM_1_2_DEPENDENCIES = "src/test/java/org/nvip/plugfest/tooling/sample_boms/proton-bridge-v1.8.0.bom.xml";

    public static final String TEST_CDX_SBOM_1_4_DEPENDENCIES = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.cdxgen.1-4.xml";


    @Test
    public void translatorcdx_small_file_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDXXML.translatorCDXXML(test_small_cdx.toString());
        assertNotNull(sbom);
        Assertions.assertEquals(SBOMType.CYCLONE_DX, sbom.getOriginFormat());
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("http://cyclonedx.org/schema/bom/1.4", sbom.getSpecVersion());
        assertEquals(18, sbom.getAllComponents().size());
    }

    @Test
    public void translatorcdx_large_file_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDXXML.translatorCDXXML(test_large_cdx.toString());
        assertNotNull(sbom);
        assertEquals(SBOMType.CYCLONE_DX, sbom.getOriginFormat());
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("http://cyclonedx.org/schema/bom/1.4", sbom.getSpecVersion());
        assertEquals(434, sbom.getAllComponents().size());
    }

    @Test
    public void translatorcdx_no_metadata_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDXXML.translatorCDXXML(test_no_metadata_cdx.toString());
        assertNull(sbom);
    }

    @Test
    public void translatorcdx_no_components_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDXXML.translatorCDXXML(test_no_components_cdx.toString());
        assertNotNull(sbom);
        // Should be 1 component for head component
        assertEquals(SBOMType.CYCLONE_DX, sbom.getOriginFormat());
        assertEquals("1", sbom.getSbomVersion());
        assertEquals("http://cyclonedx.org/schema/bom/1.4", sbom.getSpecVersion());
        assertEquals(1, sbom.getAllComponents().size());
    }

    @Test
    public void translatorcdx_v1_2_dependencies_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDXXML.translatorCDXXML(TEST_CDX_SBOM_1_2_DEPENDENCIES.toString());
        assertNotNull(sbom);
        assertEquals(202, sbom.getAllComponents().size());
    }

    @Test
    public void translatorcdx_v1_2_dependencies_other_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDXXML.translatorCDXXML(TEST_CDX_SBOM_1_4_DEPENDENCIES);
        assertNotNull(sbom);
        assertEquals(631, sbom.getAllComponents().size());
    }

    @Test
    public void translatorcdx_very_small_file_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDXXML.translatorCDXXML(
                "src/test/java/org/nvip/plugfest/tooling/sample_boms/syft-0.80.0-source-cdx-xml.xml"
        );
        assertNotNull(sbom);
        //Assertions.assertEquals(SBOMType.CYCLONE_DX, sbom.getOriginFormat());
        //assertEquals("1", sbom.getSbomVersion());
        //assertEquals("http://cyclonedx.org/schema/bom/1.4", sbom.getSpecVersion());
        //assertEquals(18, sbom.getAllComponents().size());
    }

}
