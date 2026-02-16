package org.kevinkib.statistics.business;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

class StatisticsServiceTest {

    public static StatisticsService statsService = new StatisticsService();


    private Game game(Long id, Hand playerHand, Hand dealerHand, GameResult result) {
        return new Game(
                id,
                new Participant(Role.PLAYER, playerHand, List.of()),
                new Participant(Role.DEALER, dealerHand, List.of()),
                result
        );
    }

    @Test
    void totalGames_shouldReturnCorrectCount() {
        Statistics statistics = statsService.calculate(List.of(
                game(1L, new Hand(Rank.TEN), new Hand(Rank.NINE), GameResult.WIN),
                game(2L, new Hand(Rank.EIGHT), new Hand(Rank.SEVEN), GameResult.LOSS)
        ));

        assertThat(statistics.totalGames(), is(2));
    }

    @Test
    void rateOfGameResult_shouldReturnCorrectWinRate() {
        Statistics statistics = statsService.calculate(List.of(
                game(1L, new Hand(Rank.TEN), new Hand(Rank.NINE), GameResult.WIN),
                game(2L, new Hand(Rank.EIGHT), new Hand(Rank.SEVEN), GameResult.LOSS)
        ));

        assertThat(statistics.winRate(), is(50.0));
    }

    @Test
    void rateOfGameResult_shouldReturnCorrectLossRate() {
        Statistics statistics = statsService.calculate(List.of(
                game(1L, new Hand(Rank.TEN), new Hand(Rank.NINE), GameResult.WIN),
                game(2L, new Hand(Rank.EIGHT), new Hand(Rank.SEVEN), GameResult.LOSS)
        ));

        assertThat(statistics.lossRate(), is(50.0));
    }

    @Test
    void playerBustRate_shouldReturnCorrectRate() {
        Statistics statistics = statsService.calculate(List.of(
                game(1L,
                        new Hand(Rank.KING, Rank.QUEEN, Rank.TEN), // bust
                        new Hand(Rank.NINE),
                        GameResult.LOSS),
                game(2L,
                        new Hand(Rank.TEN),
                        new Hand(Rank.SEVEN),
                        GameResult.WIN)
        ));

        assertThat(statistics.playerBustRate(), is(50.0));
    }

    @Test
    void averagePlayerScore_shouldReturnCorrectAverage() {
        Statistics statistics = statsService.calculate(List.of(
                game(1L,
                        new Hand(Rank.TEN, Rank.SEVEN), // 17
                        new Hand(Rank.NINE),
                        GameResult.WIN),
                game(2L,
                        new Hand(Rank.KING, Rank.SIX), // 16
                        new Hand(Rank.SEVEN),
                        GameResult.LOSS)
        ));

        assertThat(statistics.averagePlayerScore(), is((17 + 16) / 2.0));
    }

    @Test
    void averagePlayerScore_shouldReturnZeroWhenNoGames() {
        Statistics statistics = statsService.calculate(List.of());

        assertThat(statistics.averagePlayerScore(), is(0.0));
    }

    @Test
    void playerBustRate_shouldReturnZeroWhenNoGames() {
        Statistics statistics = statsService.calculate(List.of());

        assertThat(statistics.playerBustRate(), is(0.0));
    }
}