package org.kevinkib.statistics.business.model;

public record StatisticsReport(
        double winRate,
        double blackJackRate) {

    public static StatisticsReport empty() {
        return new StatisticsReport(0.0, 0.0);
    }
}
