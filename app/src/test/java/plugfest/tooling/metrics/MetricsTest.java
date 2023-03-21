package plugfest.tooling.metrics;

import org.junit.jupiter.api.Test;

public class MetricsTest {

    private static final String TEST_SPDX_v2_3_SBOM = "src/test/java/plugfest/tooling/sample_boms/sbom.alpine.2-3.spdx";
    @Test
    public void metrics_test() {

        Metrics metrics = new Metrics(TEST_SPDX_v2_3_SBOM);

        metrics.verify(TEST_SPDX_v2_3_SBOM);

    }

}
