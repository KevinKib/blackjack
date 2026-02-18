package org.kevinkib.statistics.business;

import org.junit.jupiter.api.Test;
import org.kevinkib.statistics.business.model.*;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class StatisticsCalculatorTest {

    public static StatisticsCalculator statsService = new StatisticsCalculator();

    private Game game(Long id, Hand playerHand, Hand dealerHand, GameResult result) {
        return new Game(
                id,
                new Participant(Role.PLAYER, playerHand),
                new Participant(Role.DEALER, dealerHand),
                result
        );
    }

    @Test
    void totalGames_shouldReturnCorrectCount() {
        StatisticsReport statisticsReport = statsService.calculate(List.of(
                game(1L, new Hand(Rank.TEN), new Hand(Rank.NINE), GameResult.WIN),
                game(2L, new Hand(Rank.EIGHT), new Hand(Rank.SEVEN), GameResult.LOSS)
        ));

        assertThat(statisticsReport.totalGames(), is(2));
    }

    @Test
    void rateOfGameResult_shouldReturnCorrectWinRate() {
        StatisticsReport statisticsReport = statsService.calculate(List.of(
                game(1L, new Hand(Rank.TEN), new Hand(Rank.NINE), GameResult.WIN),
                game(2L, new Hand(Rank.EIGHT), new Hand(Rank.SEVEN), GameResult.LOSS)
        ));

        assertThat(statisticsReport.winRate(), is(50.0));
    }

    @Test
    void rateOfGameResult_shouldReturnCorrectLossRate() {
        StatisticsReport statisticsReport = statsService.calculate(List.of(
                game(1L, new Hand(Rank.TEN), new Hand(Rank.NINE), GameResult.WIN),
                game(2L, new Hand(Rank.EIGHT), new Hand(Rank.SEVEN), GameResult.LOSS)
        ));

        assertThat(statisticsReport.lossRate(), is(50.0));
    }

    @Test
    void playerBustRate_shouldReturnCorrectRate() {
        StatisticsReport statisticsReport = statsService.calculate(List.of(
                game(1L,
                        new Hand(Rank.KING, Rank.QUEEN, Rank.TEN), // bust
                        new Hand(Rank.NINE),
                        GameResult.LOSS),
                game(2L,
                        new Hand(Rank.TEN),
                        new Hand(Rank.SEVEN),
                        GameResult.WIN)
        ));

        assertThat(statisticsReport.playerBustRate(), is(50.0));
    }

    @Test
    void averagePlayerScore_shouldReturnCorrectAverage() {
        StatisticsReport statisticsReport = statsService.calculate(List.of(
                game(1L,
                        new Hand(Rank.TEN, Rank.SEVEN), // 17
                        new Hand(Rank.NINE),
                        GameResult.WIN),
                game(2L,
                        new Hand(Rank.KING, Rank.SIX), // 16
                        new Hand(Rank.SEVEN),
                        GameResult.LOSS)
        ));

        assertThat(statisticsReport.averagePlayerScore(), is((17 + 16) / 2.0));
    }

    @Test
    void averagePlayerScore_shouldReturnZeroWhenNoGames() {
        StatisticsReport statisticsReport = statsService.calculate(List.of());

        assertThat(statisticsReport.averagePlayerScore(), is(0.0));
    }

    @Test
    void playerBustRate_shouldReturnZeroWhenNoGames() {
        StatisticsReport statisticsReport = statsService.calculate(List.of());

        assertThat(statisticsReport.playerBustRate(), is(0.0));
    }
}