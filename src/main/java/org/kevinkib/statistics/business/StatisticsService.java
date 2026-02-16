package org.kevinkib.statistics.business;

import java.util.List;

public class StatisticsService {

    public StatisticsService() {
    }

    public Statistics calculate(List<Game> games) {
        if (games.isEmpty()) {
            return new Statistics(0, 0.0, 0.0, 0.0, 0.0);
        }

        int totalGames = games.size();

        double winRate = 100.0 * games.stream()
                .filter(game -> GameResult.WIN.equals(game.result()))
                .count() / totalGames;

        double lossRate = 100.0 * games.stream()
                .filter(game -> GameResult.LOSS.equals(game.result()))
                .count() / totalGames;

        double averagePlayerScore = games.stream()
                .mapToInt(game -> game.player().hand().score())
                .average()
                .orElse(0.0);

        double playerBustRate = 100.0 * games.stream()
                .filter(game -> game.player().hand().isBust())
                .count() / totalGames;

        return new Statistics(totalGames, winRate, lossRate, averagePlayerScore, playerBustRate);
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
