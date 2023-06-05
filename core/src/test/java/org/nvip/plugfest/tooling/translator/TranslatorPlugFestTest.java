package org.nvip.plugfest.tooling.translator;

import org.junit.jupiter.api.*;
import org.nvip.plugfest.tooling.sbom.SBOM;

import java.awt.desktop.ScreenSleepEvent;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * File: TranslatorPlugFestTest.java
 * Tests for TranslatorPlugFest
 *
 * @author Tyler Drake
 * @author Asa Horn
 * @author Matt London
 */
public class TranslatorPlugFestTest {

    /**
     * Test Constants
     */
    private static final String TEST_JSON ="src/test/java/org/nvip/plugfest/tooling/sample_boms/cdx_json/trivy-0.39.0_celery-3.1.cdx.json";

    private static final String TEST_XML = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.alpine.xml";

    private static final String TEST_SPDX = "src/test/java/org/nvip/plugfest/tooling/sample_boms/sbom.docker.2-2.spdx";

    /**
     * Expected Results
     */
    private static final int EXPECTED_XML_COMPONENTS = 18;

    private static final int EXPECTED_JSON_COMPONENTS = 124;

    private static final int EXPECTED_SPDX_COMPONENTS = 138;

    @Test
    public void driver_translates_xml() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_XML);
        assertNotNull(sbom);
        assertEquals(EXPECTED_XML_COMPONENTS, sbom.getAllComponents().size());
    }

    @Test
    public void driver_translates_json() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_JSON);
        assertNotNull(sbom);
        assertEquals(EXPECTED_JSON_COMPONENTS, sbom.getAllComponents().size());
    }

    @Test
    public void driver_translates_spdx() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_SPDX);
        assertNotNull(sbom);
        assertEquals(EXPECTED_SPDX_COMPONENTS, sbom.getAllComponents().size());
    }

    @Test
    public void driver_translates_xml_content() throws TranslatorException {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(TEST_XML)));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        SBOM sbom = TranslatorPlugFest.translateContents(content, TEST_XML);
        assertNotNull(sbom);
        assertEquals(EXPECTED_XML_COMPONENTS, sbom.getAllComponents().size());
    }

    @Test
    public void driver_translates_json_content() throws TranslatorException {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(TEST_JSON)));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        SBOM sbom = TranslatorPlugFest.translateContents(content, TEST_JSON);
        assertNotNull(sbom);
        assertEquals(EXPECTED_JSON_COMPONENTS, sbom.getAllComponents().size());
    }

    @Test
    public void driver_translates_spdx_content() throws TranslatorException {
        String content = null;
        try {
            content = new String(Files.readAllBytes(Paths.get(TEST_SPDX)));
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
        SBOM sbom = TranslatorPlugFest.translateContents(content, TEST_SPDX);
        assertNotNull(sbom);
        assertEquals(EXPECTED_SPDX_COMPONENTS, sbom.getAllComponents().size());
    }


    @Test
    public void driver_translates_xml_supplier() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_XML);
        assertNotNull(sbom);
        assertEquals("anchore", sbom.getMetadata().getSuppliers());
    }

    @Test
    public void driver_translates_json_tool() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_JSON);
        assertNotNull(sbom);
        assertEquals("Tool: aquasecurity trivy-0.39.0", sbom.getMetadata().getTools().get("trivy"));
    }

    @Test
    public void driver_translates_spdx_tool() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_SPDX);
        assertNotNull(sbom);
        assertEquals("Tool: spdx-sbom-generator-source-code", sbom.getMetadata().getTools().get("spdx-sbom-generator-source-code"));
    }

    @Test
    public void driver_translates_xml_timestamp() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_XML);
        assertNotNull(sbom);
        assertEquals("2023-02-21T08:50:33-05:00", sbom.getMetadata().getTimestamp());
    }

    @Test
    @Disabled
    public void driver_translates_json_timestamp() throws TranslatorException {
        // todo fix -fails ci/cd b/c this is in EDT and ci/cd defaults to UTC
        SBOM sbom = TranslatorPlugFest.translate(TEST_JSON);
        assertNotNull(sbom);
        assertEquals("2023-04-05T16:49:04+00:00", sbom.getMetadata().getTimestamp());
    }

    @Test
    public void driver_translates_spdx_timestamp() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_SPDX);
        assertNotNull(sbom);
        assertEquals("2023-03-10T18:48:20Z", sbom.getMetadata().getTimestamp());
    }

    @Test
    public void driver_translates_xml_format() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_XML);
        assertNotNull(sbom);
        assertEquals("CYCLONE_DX", sbom.getOriginFormat().toString());
    }

    @Test
    public void driver_translates_json_format() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_JSON);
        assertNotNull(sbom);
        assertEquals("CYCLONE_DX", sbom.getOriginFormat().toString());
    }

    @Test
    public void driver_translates_spdx_format() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_SPDX);
        assertNotNull(sbom);
        assertEquals("SPDX", sbom.getOriginFormat().toString());
    }

    @Test
    public void driver_translates_xml_SBOM_version() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_XML);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
    }

    @Test
    public void driver_translates_json_SBOM_version() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_JSON);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
    }

    @Test
    public void driver_translates_spdx_SBOM_version() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_SPDX);
        assertNotNull(sbom);
        assertEquals("1", sbom.getSbomVersion());
    }

    @Test
    public void driver_translates_xml_spec_version() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_XML);
        assertNotNull(sbom);
        assertEquals("http://cyclonedx.org/schema/bom/1.4", sbom.getSpecVersion());
    }

    @Test
    public void driver_translates_json_spec_version() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_JSON);
        assertNotNull(sbom);
        assertEquals("1.4", sbom.getSpecVersion());
    }

    @Test
    public void driver_translates_spdx_spec_version() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_SPDX);
        assertNotNull(sbom);
        assertEquals("SPDX-2.2", sbom.getSpecVersion());
    }

    @Test
    public void driver_translates_xml_children() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_XML);
        assertNotNull(sbom);
        assertNotNull(sbom.getHeadUUID());
        assertEquals(17, sbom.getChildrenUUIDs(sbom.getHeadUUID()).size());
    }

    @Test
    public void driver_translates_json_spec_children() throws TranslatorException {
        SBOM sbom1 = TranslatorPlugFest.translate(TEST_JSON);
        assertNotNull(sbom1);
        assertNotNull(sbom1.getHeadUUID());
        assertEquals(123, sbom1.getChildrenUUIDs(sbom1.getHeadUUID()).size());
    }

    @Test
    public void driver_translates_spdx_children() throws TranslatorException {
        SBOM sbom = TranslatorPlugFest.translate(TEST_SPDX);
        assertNotNull(sbom);
        assertNotNull(sbom.getHeadUUID());
        assertEquals(137, sbom.getChildrenUUIDs(sbom.getHeadUUID()).size());
    }
}
