package org.kevinkib.statistics.business.model;

public record Statistics(
        int totalGames,
        double winRate,
        double lossRate,
        double averagePlayerScore,
        double playerBustRate) {

}
