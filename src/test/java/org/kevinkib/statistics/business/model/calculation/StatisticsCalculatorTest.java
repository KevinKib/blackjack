package org.kevinkib.statistics.business.model.calculation;

import org.junit.jupiter.api.Test;
import org.kevinkib.statistics.business.model.game.Game;
import org.kevinkib.statistics.business.model.game.HandBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.kevinkib.statistics.business.model.game.GameBuilder.aGame;
import static org.kevinkib.statistics.business.model.game.GameOutcome.*;
import static org.kevinkib.statistics.business.model.game.HandBuilder.aHand;


public class StatisticsCalculatorTest {

    private final StatisticsCalculator statisticsCalculator = new StatisticsCalculator();

    @Test
    public void canCalculateNumberOfGames() {
        int nbGames = 5;
        List<Game> games = new ArrayList<>();

        for (int i = 0; i < nbGames; ++i) {
            games.add(aGame().build());
        }

        StatisticsReport report = statisticsCalculator.getStatisticsReport(games);
        assertThat(report.nbGames(), is(nbGames));
    }

    @Test
    public void canCalculateWinRateOfGames() {
        int nbWonGames = 3;
        int nbLostGames = 2;
        int nbDrewGames = 2;
        List<Game> games = new ArrayList<>();

        for (int i = 0; i < nbWonGames; ++i) {
            games.add(aGame().withOutcome(WIN).build());
        }
        for (int i = 0; i < nbLostGames; ++i) {
            games.add(aGame().withOutcome(LOSS).build());
        }
        for (int i = 0; i < nbDrewGames; ++i) {
            games.add(aGame().withOutcome(DRAW).build());
        }

        double expectedWinRate = (double) nbWonGames / (nbWonGames + nbDrewGames + nbLostGames) * 100;

        StatisticsReport report = statisticsCalculator.getStatisticsReport(games);
        assertThat(report.winRate(), is(expectedWinRate));
    }

    @Test
    public void givenZeroGames_thenWinRateEqualsZero() {
        List<Game> games = new ArrayList<>();

        double expectedWinRate = 0.0;

        StatisticsReport report = statisticsCalculator.getStatisticsReport(games);
        assertThat(report.winRate(), is(expectedWinRate));
    }

    @Test
    public void canCalculateAverageScoreOfGames() {
        List<Game> games = new ArrayList<>();
        int scoreA = 19;
        int scoreB = 15;
        int scoreC = 21;
        int nbGames = 3;

        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreA).build()
        ).build());
        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreB).build()
        ).build());
        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreC).build()
        ).build());

        double expectedAverageScore = (double) (scoreA + scoreB + scoreC) / nbGames;

        StatisticsReport report = statisticsCalculator.getStatisticsReport(games);
        assertThat(report.playerScoreStatistics().average(), is(expectedAverageScore));
    }

    @Test
    public void canCalculateBustRateOfGames() {
        List<Game> games = new ArrayList<>();
        int scoreA = 22;
        int scoreB = 10;
        int scoreC = 26;
        int nbGames = 3;

        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreA).build()
        ).build());
        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreB).build()
        ).build());
        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreC).build()
        ).build());

        double expectedBustRate = (double) 2 / nbGames;

        StatisticsReport report = statisticsCalculator.getStatisticsReport(games);
        assertThat(report.playerScoreStatistics().bustRate(), is(expectedBustRate));
    }

    @Test
    public void canCalculateBlackjackRateOfGames() {
        List<Game> games = new ArrayList<>();
        int scoreA = 21;
        int blackjackNbCards = 2;
        int scoreB = 16;
        int nonBlackjackNbCards = 3;
        int scoreC = 20;
        int nbGames = 3;

        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreA).withNbCards(blackjackNbCards).build()
        ).build());
        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreB).withNbCards(nonBlackjackNbCards).build()
        ).build());
        games.add(aGame().withPlayerHand(
                aHand().withScore(scoreC).withNbCards(nonBlackjackNbCards).build()
        ).build());

        double expectedBlackjackRate = (double) 1 / nbGames;

        StatisticsReport report = statisticsCalculator.getStatisticsReport(games);
        assertThat(report.playerScoreStatistics().blackJackRate(), is(expectedBlackjackRate));
    }

}
