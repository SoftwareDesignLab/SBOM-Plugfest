/**
 * @file TranslatorCDXTest.java
 *
 * Test set for TranslatorCDX class
 *
 * @author Tyler Drake
 */

package org.nvip.plugfest.tooling.sbom;

import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.translator.TranslatorCDX;
import org.nvip.plugfest.tooling.translator.TranslatorSPDX;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class CDXParserTest {

    public static final String test_small_cdx = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine.xml";
    public static final String test_large_cdx = "src/test/java/plugfest/tooling/sample_boms/sbom.python.xml";
    public static final String test_no_metadata_cdx = "src/test/java/plugfest/tooling/sample_boms/sbom.nometadata.xml";
    public static final String test_no_components_cdx = "src/test/java/plugfest/tooling/sample_boms/sbom.nocomponents.xml";


    @Test
    public void translatorcdx_small_file_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDX.translatorCDX(test_small_cdx.toString());
        assertNotNull(sbom);
    }

    @Test
    public void translatorcdx_large_file_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDX.translatorCDX(test_large_cdx.toString());
        assertNotNull(sbom);
    }

    @Test
    public void translatorcdx_no_metadata_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDX.translatorCDX(test_no_metadata_cdx.toString());
        assertNull(sbom);
    }

    @Test
    public void translatorcdx_no_components_test() throws ParserConfigurationException {
        SBOM sbom = TranslatorCDX.translatorCDX(test_no_components_cdx.toString());
        assertNotNull(sbom);
        // Should be 1 component for head component
        assertEquals(1, sbom.getAllComponents().size());
    }

}
