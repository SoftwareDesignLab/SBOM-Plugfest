package plugfest.tooling.qa;

public class QualityReport {
    private int score = 0;
    // include data on what tests were run
    public QualityReport(){

    }

    public void updateScore(int increment) {
        this.score += increment;
    }
    public int getScore() {
        return this.score;
    }
}
