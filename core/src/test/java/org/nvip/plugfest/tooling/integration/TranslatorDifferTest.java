//package org.nvip.plugfest.tooling.integration;
//
//import org.junit.jupiter.api.Test;
//import org.nvip.plugfest.tooling.differ.Comparer;
//import org.nvip.plugfest.tooling.differ.DiffReport;
//import org.nvip.plugfest.tooling.sbom.SBOM;
//import org.nvip.plugfest.tooling.translator.TranslatorCDXXML;
//import org.nvip.plugfest.tooling.translator.TranslatorPlugFest;
//import org.nvip.plugfest.tooling.translator.TranslatorSPDX;
//
//import javax.xml.parsers.ParserConfigurationException;
//import java.io.IOException;
//
//import static org.junit.jupiter.api.Assertions.*;
//
///**
// * File: TranslatorDifferTest.java
// * Integration tests hitting both the translator package and the differ package
// *
// * @author Tyler Drake
// */
//public class TranslatorDifferTest {
//
//    private static final String TEST_SPDX_v2_3_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine.2-3.spdx";
//
//    private static final String TEST_SPDX_v2_3_DIFF_BOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine-compare.2-3.spdx";
//
//    private static final String TEST_SPDX_v2_2_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.docker.2-2.spdx";
//
//    private static final String TEST_SPDX_LARGE_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.python.2-3.spdx";
//
//    private static final String TEST_SPDX_EMPTY = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.empty.2-3.spdx";
//
//    private static final String TEST_CDX_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine.xml";
//
//    private static final String TEST_CDX_DIFF_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine-compare.xml";
//
//    private static final String TEST_CDX_LARGE_SBOM = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.python.xml";
//
//    private static final int EXPECTED_CONFLICTS_SMALL_LARGE_SPDX = 447;
//
//    private static final int EXPECTED_CONFLICTS_SMALL_LARGE_CDX = 449;
//
//    private static final String EXPECTED_VERSION_MISTMATCH_SPDX_2_3_SPDX_2_2
//            = "Schema Version Mismatch:\n" +
//            "+ SPDX-2.3\n" +
//            "- SPDX-2.2";
//
//    private static final String EXPECTED_SCHEMA_MISMATCH_SPDX_CDX
//            = "Origin Format Mismatch:\n" +
//            "+ SPDX\n" +
//            "- CYCLONE_DX";
//
//    private static final String EXPECTED_VERSION_MISMATCH_SPDX_2_3_CDX_1_4
//            = "Schema Version Mismatch:\n" +
//            "+ SPDX-2.3\n" +
//            "- http://cyclonedx.org/schema/bom/1.4";
//
//
//    /**
//     * SPDX Translator -> Differ Tests
//     */
//
//    @Test
//    public void full_diff_report_from_SPDX_SBOMs() throws IOException {
//
//        // Create first SBOM
//        SBOM test_sbom_one = TranslatorPlugFest.translate(TEST_SPDX_v2_3_SBOM);
//        assertNotNull(test_sbom_one);
//
//        // Create second SBOM
//        SBOM test_sbom_two = TranslatorPlugFest.translate(TEST_SPDX_v2_3_DIFF_BOM);
//        assertNotNull(test_sbom_two);
//
//        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);
//
//        assertNotNull(test_report);
//    }
//
//    @Test
//    public void full_diff_report_from_v2_3_SPDX_and_v2_2_SPDX() throws IOException {
//        // Create first SBOM
//        SBOM test_sbom_one = TranslatorPlugFest.translate(TEST_SPDX_v2_3_SBOM);
//        assertNotNull(test_sbom_one);
//
//        // Create second SBOM
//        SBOM test_sbom_two = TranslatorPlugFest.translate(TEST_SPDX_v2_2_SBOM);
//        assertNotNull(test_sbom_two);
//
//        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);
//
//        assertNotNull(test_report);
//        assertTrue(test_report.getSbomConflict().toString().contains(EXPECTED_VERSION_MISTMATCH_SPDX_2_3_SPDX_2_2));
//    }
//
//    @Test
//    public void full_diff_report_from_v2_3_SPDX_and_empty_SPDX() throws IOException {
//
//        // Create first SBOM
//        SBOM test_sbom_one = TranslatorPlugFest.translate(TEST_SPDX_v2_3_SBOM);
//        assertNotNull(test_sbom_one);
//
//        // Create second SBOM
//        SBOM test_sbom_two = TranslatorPlugFest.translate(TEST_SPDX_EMPTY);
//        assertNotNull(test_sbom_two);
//
//        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);
//
//        assertNotNull(test_report);
//        assertEquals(17, test_report.getComponentConflicts().size());
//
//
//    }
//
//    @Test
//    public void full_diff_report_from_small_SPDX_SBOM_and_large_SPDX_SBOM() throws IOException {
//
//        SBOM test_sbom_one = TranslatorPlugFest.translate(TEST_SPDX_LARGE_SBOM);
//        assertNotNull(test_sbom_one);
//
//        // Create second SBOM
//        SBOM test_sbom_two = TranslatorPlugFest.translate(TEST_SPDX_v2_3_SBOM);
//        assertNotNull(test_sbom_two);
//
//        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);
//
//        assertNotNull(test_report);
//
//        // There are 447 components that exist in python file that don't exist in alpine file
//        assertEquals(EXPECTED_CONFLICTS_SMALL_LARGE_SPDX, test_report.getComponentConflicts().size());
//
//    }
//
//    /**
//     * CDX Translator -> Differ Tests
//     */
//
//    @Test
//    public void full_diff_report_from_CDX_SBOM() throws ParserConfigurationException {
//
//        // Create first SBOM
//        SBOM test_sbom_one = TranslatorPlugFest.translate(TEST_CDX_SBOM);
//        assertNotNull(test_sbom_one);
//
//        // Create second SBOM
//        SBOM test_sbom_two = TranslatorPlugFest.translate(TEST_CDX_DIFF_SBOM);
//        assertNotNull(test_sbom_two);
//
//        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);
//
//        assertNotNull(test_report);
//
//    }
//
//    @Test
//    public void full_diff_report_from_small_CDX_SBOM_and_large_CDX_SBOM() throws ParserConfigurationException {
//
//        SBOM test_sbom_one = TranslatorPlugFest.translate(TEST_CDX_LARGE_SBOM);
//        assertNotNull(test_sbom_one);
//
//        // Create second SBOM
//        SBOM test_sbom_two = TranslatorPlugFest.translate(TEST_CDX_SBOM);
//        assertNotNull(test_sbom_two);
//
//        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);
//
//        assertNotNull(test_report);
//
//        // There are 447 components that exist in python file that don't exist in alpine file
//        assertEquals(EXPECTED_CONFLICTS_SMALL_LARGE_CDX, test_report.getComponentConflicts().size());
//    }
//
//    /**
//     * SPDX + CDX Translator -> Differ Tests
//     */
//
//    @Test
//    public void full_diff_report_from_SPDX_and_CDX() throws IOException, ParserConfigurationException {
//
//        // Create an SPDX SBOM
//        SBOM test_sbom_one = TranslatorPlugFest.translate(TEST_SPDX_v2_3_SBOM);
//        assertNotNull(test_sbom_one);
//
//        // Create a CDX SBOM
//        SBOM test_sbom_two = TranslatorPlugFest.translate(TEST_CDX_SBOM);
//        assertNotNull(test_sbom_two);
//
//        // Make a diff report of the SPDX against the CDX SBOM
//        DiffReport test_report = Comparer.generateReport(test_sbom_one, test_sbom_two);
//
//        assertNotNull(test_report);
//
//        // There should be at least a schema mismatch due to one file being SPDX and the other being CDX
//        assertTrue(test_report.getSbomConflict().toString().contains(EXPECTED_SCHEMA_MISMATCH_SPDX_CDX));
//
//        // The versions should also be mismatching
//        assertTrue(test_report.getSbomConflict().toString().contains(EXPECTED_VERSION_MISMATCH_SPDX_2_3_CDX_1_4));
//
//    }
//}
