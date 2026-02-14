package org.kevinkib.statistics.business;

import java.util.List;

public record Statistics(List<Game> games) {

    public Statistics {
        games = List.copyOf(games);
    }

    public long totalGames() {
        return games.size();
    }

    public double rateOfGameResult(GameResult result) {
        return 100.0 * games.stream()
                .filter(game -> result.equals(game.result()))
                .count() / totalGames();
    }

    public double averagePlayerScore() {
        return games.stream()
                .mapToInt(game -> game.player().hand().score())
                .average()
                .orElse(0.0);
    }

    public double playerBustRate() {
        return 100.0 * games.stream()
                .filter(game -> game.player().hand().isBust())
                .count() / totalGames();
    }

    public double winRateWhenStandingAt(int standScore) {
        long standingGames = games.stream()
                .filter(game -> game.player().hand().score() == standScore)
                .count();

        if (standingGames == 0) return 0.0;
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
