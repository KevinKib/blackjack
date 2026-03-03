package org.kevinkib.statistics.business.model.calculation;

public record StatisticsReport(
        double winRate,
        double average,
        double bustRate,
        double blackJackRate) {

    public static StatisticsReport empty() {
        return new StatisticsReport(0.0, 0.0, 0.0, 0.0);
    }
}
