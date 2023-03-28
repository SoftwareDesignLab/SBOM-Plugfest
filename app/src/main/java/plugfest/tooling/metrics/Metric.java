package plugfest.tooling.metrics;

public abstract class Metric {
    private int score;
    public int getScore() { return this.score; }
    protected abstract int testMetric();
}