package org.nvip.plugfest.tooling.metrics;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * TODO: timestampConsistencyMetric isn't used much. Revisit this when it's being used more or receives more development.
 */

public class timestampConsistencyMetricTest {

    /**
     * Test Constants
     */

    private static final String TEST_TIMESTAMP_ONE = "2023-01-01T00:00:00Z";

    /**
     * timestampConsistencyMetric Constructor Test
     */

    @Test
    public void create_timestampConsistencyMetric() {
        timestampConsistencyMetric test_tc_metric = new timestampConsistencyMetric(TEST_TIMESTAMP_ONE);
        assertNotNull(test_tc_metric);
    }

}
