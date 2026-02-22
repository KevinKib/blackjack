package org.kevinkib.statistics;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.kevinkib.BlackJackService;
import org.kevinkib.GameState;
import org.kevinkib.statistics.presentation.v1.StatisticsInternalController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.time.LocalDate;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.junit.MatcherAssert.assertThat;

@SpringBootTest
public class StatisticsInternalControllerIT {

    @Autowired
    private StatisticsInternalController statisticsInternalController;

    private static JdbcTemplate jdbcTemplate;
    private final LocalDate date = LocalDate.now();

    @BeforeAll
    public static void before() {
        jdbcTemplate = new JdbcTemplate(BlackJackService.getTestDataSource());
    }

    @AfterEach
    public void clearDatabase() {
        jdbcTemplate.update("DELETE FROM PILE;");
        jdbcTemplate.update("DELETE FROM GAME;");
    }

    @Test
    public void whenGameStarts_thenShowCorrectNumberOfGames() {
        int expectedNumberOfGames = 6;

        for (int i = 0; i < expectedNumberOfGames; ++i) {
            createGameInDatabase(date, GameState.WIN);
        }

        int nbGames = statisticsInternalController.getStatisticsReport().nbGames();

        assertThat(nbGames, is(expectedNumberOfGames));
    }

    @Test
    public void whenGameStarts_thenCalculateCorrectWinPercentage() {
        int wonGames = 2;
        int lostGames = 3;

        for (int i = 0; i < wonGames; ++i) {
            createGameInDatabase(date, GameState.WIN);
        }

        for (int i = 0; i < lostGames; ++i) {
            createGameInDatabase(date, GameState.LOSE);
        }

        double winRate = statisticsInternalController.getStatisticsReport().winRate();
        double expectedWinRate = (double) wonGames / (wonGames + lostGames) * 100;

        assertThat(winRate, is(expectedWinRate));
    }

    private Long createGameInDatabase(LocalDate date, GameState status) {
        String sql = "INSERT INTO GAME (GAME_CREATION_DATE, GAME_STATE) VALUES (?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setDate(1, java.sql.Date.valueOf(date));
            ps.setString(2, status.name());
            return ps;
        }, keyHolder);

        return keyHolder.getKey().longValue();
    }
}