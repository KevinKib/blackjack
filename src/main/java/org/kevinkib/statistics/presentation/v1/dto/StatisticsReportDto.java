package org.kevinkib.statistics.presentation.v1.dto;

public record StatisticsReportDto(
        int nbGames,
        double winRate,
        double averagePlayerScore,
        double playerBustRate,
        double blackJackRate
) {

    public boolean isEmpty() {
        return nbGames == 0;
    }

}
