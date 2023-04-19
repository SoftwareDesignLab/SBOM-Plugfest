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

/**
 * Metric to test if the timestamp is in the correct format
 *
 * @author Derek Garcia
 */
public class timestampConsistencyMetric extends Metric {
    private final String timestamp;   // Metric related field etc

    /**
     * Constructor for timestampConsistencyMetric
     *
     * @param timestamp - the timestamp to be tested
     */
    public timestampConsistencyMetric(String timestamp) {
        this.timestamp = timestamp;
        this.score = testMetric(); //run the test
    }

    /**
     * Test if the timestamp is in the correct format
     *
     * @return - Integer score this SBOM received for this metric. (1 pass, 0 fail)
     */
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
