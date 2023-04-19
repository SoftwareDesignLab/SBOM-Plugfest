package org.nvip.plugfest.tooling.metrics;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Abstract class for metrics
 *
 * @author (names)
 */
public abstract class Metric {
    /**
     * The score the SBOM received for this metric.
     */
    protected int score;

    /**
     * Implementations should override this function to run the actual tests. Consider calling smaller tests from this
     * main function.
     *
     * @return - Integer score this SBOM received for this metric.
     */
    protected abstract int testMetric();

    //getter
    public int getScore() {
        return this.score;
    }
}
