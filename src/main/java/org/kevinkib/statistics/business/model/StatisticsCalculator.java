package org.kevinkib.statistics.business.model;

import java.util.List;

public class StatisticsCalculator {

    public StatisticsCalculator() {
    }

    public StatisticsReport calculate(List<Game> games) {
        if (games.isEmpty()) {
            return StatisticsReport.empty();
        }

        int totalGames = games.size();

        double winRate = percentage(
                games.stream()
                    .filter(game -> GameResult.WIN.equals(game.result()))
                    .count(),
                totalGames);

        double lossRate = percentage(
                games.stream()
                    .filter(game -> GameResult.LOSS.equals(game.result()))
                    .count(),
                totalGames);

        double averagePlayerScore = games.stream()
                .mapToInt(game -> game.player().hand().score())
                .average()
                .orElse(0.0);

        double playerBustRate = percentage(
                games.stream()
                    .filter(game -> game.player().hand().isBust())
                    .count(),
                totalGames);

        return new StatisticsReport(totalGames, winRate, lossRate, averagePlayerScore, playerBustRate);
    }

    private double percentage(long part, long total) {
        if (total == 0) {
            return 0.0;
        }
        return 100.0 * part / total;
    }

    public double winRateWhenStandingAt(int standScore) {
       /*long standingGames = games.stream()
                .filter(game -> game.player().hand().score() == standScore)
                .count();

        if (standingGames == 0) return 0.0;*/
        /*

        long wins = game.stream()
                .filter(g -> g.playerHand().score() == standScore && g.playerHits() == 0)
                .filter(g -> g.outcome() == Outcome.WIN)
                .count();

        return 100.0 * wins / standingGames;
        */
        return 0.0;
    }

}
