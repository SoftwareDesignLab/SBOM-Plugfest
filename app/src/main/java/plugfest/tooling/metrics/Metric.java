package plugfest.tooling.metrics;

public abstract class Metric {
    protected int score;
    public int getScore() {
        return this.score;
    }
    protected abstract int testMetric();
}