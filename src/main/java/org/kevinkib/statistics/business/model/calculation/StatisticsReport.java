package org.kevinkib.statistics.business.model.calculation;

public record StatisticsReport(
        int nbGames,
        double winRate,
        ScoreStatistics playerScoreStatistics) {

    public static StatisticsReport empty() {
        return new StatisticsReport(0, 0.0, ScoreStatistics.empty());
    }

}
