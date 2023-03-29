package plugfest.tooling.metrics;

public abstract class Metric {
    protected int score;
    public int getScore() {
        return this.score;
    }
    protected abstract int testMetric();
}
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