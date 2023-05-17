package org.nvip.plugfest.tooling.integration;

import org.cyclonedx.exception.ParseException;
import org.junit.jupiter.api.Test;
import org.nvip.plugfest.tooling.differ.Comparison;
import org.nvip.plugfest.tooling.sbom.SBOM;
import org.nvip.plugfest.tooling.translator.TranslatorCDXJSON;
import org.nvip.plugfest.tooling.translator.TranslatorCDXXML;

import javax.xml.parsers.ParserConfigurationException;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TranslatorComparisonTest {

    private static final String TEST_SMALL_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/sbom.alpine.json";

    private static final String TEST_MEDIUM_CDX_JSON = "src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/trivy-0.39.0_celery-3.1.cdx.json";

    private static final String TEST_SMALL_CDX_XML = "src/test/java/org/nvip/plugfest/tooling/sample_boms/syft-0.80.0-source-cdx-xml.xml";

    private static final String TEST_MEDIUM_CDX_XML = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine.xml";

    @Test
    public void translator_comparison_cdx_json_test() throws ParseException {


        SBOM test_sbom_one = TranslatorCDXJSON.translatorCDXJSON(TEST_MEDIUM_CDX_JSON);

        assertNotNull(test_sbom_one);
        assertEquals("1", test_sbom_one.getSbomVersion());
        assertEquals("1.4", test_sbom_one.getSpecVersion());
        assertEquals(124, test_sbom_one.getAllComponents().size());

        SBOM test_sbom_two = TranslatorCDXJSON.translatorCDXJSON(TEST_SMALL_CDX_JSON);

        assertNotNull(test_sbom_two);
        assertEquals("1", test_sbom_two.getSbomVersion());
        assertEquals("1.4", test_sbom_two.getSpecVersion());
        assertEquals(18, test_sbom_two.getAllComponents().size());

        Comparison test_comparison = new Comparison(Arrays.asList(test_sbom_one, test_sbom_one));

        test_comparison.runComparison();

        assertNotNull(test_comparison);
        test_comparison.getComparisons();

    }

    @Test
    public void translator_comparison_cdx_xml_test() throws ParserConfigurationException {

        SBOM test_sbom_one = TranslatorCDXXML.translatorCDXXML(TEST_SMALL_CDX_XML);

        assertNotNull(test_sbom_one);
        assertEquals("1", test_sbom_one.getSbomVersion());
        assertEquals("http://cyclonedx.org/schema/bom/1.4", test_sbom_one.getSpecVersion());
        assertEquals(10, test_sbom_one.getAllComponents().size());

        SBOM test_sbom_two = TranslatorCDXXML.translatorCDXXML(TEST_MEDIUM_CDX_XML);

        assertNotNull(test_sbom_two);
        assertEquals("1", test_sbom_two.getSbomVersion());
        assertEquals("http://cyclonedx.org/schema/bom/1.4", test_sbom_two.getSpecVersion());
        assertEquals(18, test_sbom_two.getAllComponents().size());

        Comparison test_comparison = new Comparison(Arrays.asList(test_sbom_one, test_sbom_one));

        test_comparison.runComparison();

        assertNotNull(test_comparison);
        test_comparison.getComparisons();

    }

}
