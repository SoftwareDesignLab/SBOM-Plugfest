package org.nvip.plugfest.tooling.metrics;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Metric {
    protected int score;
    public int getScore() {
        return this.score;
    }
    protected abstract int testMetric();
}
