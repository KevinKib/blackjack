package org.kevinkib.statistics.business.model;

public record StatisticsReport(
        int totalGames,
        double winRate,
        double lossRate,
        double averagePlayerScore,
        double playerBustRate) {

    public static StatisticsReport empty() {
       return new StatisticsReport(0, 0.0, 0.0, 0.0, 0.0);
    }

}
