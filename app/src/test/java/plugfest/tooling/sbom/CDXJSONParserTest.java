package plugfest.tooling.sbom;

import org.cyclonedx.exception.ParseException;
import org.junit.jupiter.api.Test;
import plugfest.tooling.translator.TranslatorCDXJSON;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class CDXJSONParserTest {

    public static final String TEST_SMALL_CDX_JSON = "src/test/java/plugfest/tooling/sample_boms/cdx_json/sbom.alpine.json";

    public static final String TEST_MEDIUM_CDX_JSON ="src/test/java/plugfest/tooling/sample_boms/cdx_json/trivy-0.39.0_celery-3.1.cdx.json";

    @Test
    public void build_SBOM_from_small_cdx_json_test() throws IOException, ParseException {
        SBOM sbom = TranslatorCDXJSON.translatorCDXJSON(TEST_SMALL_CDX_JSON);
        assertNotNull(sbom);
    }

    @Test
    public void build_SBOM_from_medium_cdx_json_test() throws IOException, ParseException {
        SBOM sbom = TranslatorCDXJSON.translatorCDXJSON(TEST_MEDIUM_CDX_JSON);
        assertNotNull(sbom);
    }

}
