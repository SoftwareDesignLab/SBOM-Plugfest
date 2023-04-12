package org.nvip.plugfest.tooling.metrics;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Example Metric
/*
public class fooMetric extends Metric {
    private String detailN;   // Metric related field etc
    public fooMetric(args to test){
        this.argn = detailN;
        this.score = testMetric();
    }
    @override
    protected int testMetric(){
        // metric implementation here
        return result;
    }
}
 */
public class timestampConsistencyMetric extends Metric {
    private final String timestamp;   // Metric related field etc

    public timestampConsistencyMetric(String timestamp) {
        this.timestamp = timestamp;
        this.score = testMetric();
    }

    @Override
    protected int testMetric() {
        // metric implementation here
        try {
            LocalDateTime.parse(this.timestamp, DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm:ss:SSS"));
        } catch (Exception e) {
            return 0;
        }
        return 1;
    }
}
