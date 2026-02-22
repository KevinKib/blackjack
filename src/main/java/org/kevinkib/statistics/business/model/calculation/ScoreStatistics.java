package org.kevinkib.statistics.business.model.calculation;

public record ScoreStatistics(
        double average,
        double bustRate,
        double blackJackRate) {

    public static ScoreStatistics empty() {
        return new ScoreStatistics(0.0, 0.0, 0.0);
    }
}
