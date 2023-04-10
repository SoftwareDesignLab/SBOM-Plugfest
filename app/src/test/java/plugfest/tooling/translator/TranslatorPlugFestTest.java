package plugfest.tooling.translator;

import org.junit.jupiter.api.Test;
import plugfest.tooling.sbom.SBOM;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class TranslatorPlugFestTest {

    public static final String TEST_JSON ="src/test/java/plugfest/tooling/sample_boms/cdx_json/trivy-0.39.0_celery-3.1.cdx.json";
    public static final String TEST_XML = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine.xml";
    private static final String TEST_SPDX = "src/test/java/plugfest/tooling/sample_boms/sbom.docker.2-2.spdx";


    @Test
    public void driver_translates_xml() {
        SBOM sbom = TranslatorPlugFest.translate(TEST_XML);
        assertNotNull(sbom);
    }

    @Test
    public void driver_translates_json() {
        SBOM sbom = TranslatorPlugFest.translate(TEST_JSON);
        assertNotNull(sbom);
    }

    @Test
    public void driver_translates_spdx() {
        SBOM sbom = TranslatorPlugFest.translate(TEST_SPDX);
        assertNotNull(sbom);
    }


}
