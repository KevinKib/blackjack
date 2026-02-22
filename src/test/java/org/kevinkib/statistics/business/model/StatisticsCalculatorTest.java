package org.kevinkib.statistics.business.model;

import org.junit.jupiter.api.Test;
import org.kevinkib.statistics.business.model.calculation.StatisticsCalculator;
import org.kevinkib.statistics.business.model.calculation.StatisticsReport;
import org.kevinkib.statistics.business.model.game.Game;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;
import static org.kevinkib.statistics.business.model.GameBuilder.aGame;
import static org.kevinkib.statistics.business.model.game.GameOutcome.*;


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

}
