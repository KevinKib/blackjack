package org.kevinkib.statistics;

import org.junit.Ignore;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.kevinkib.BlackJackService;
import org.kevinkib.GameState;
import org.kevinkib.statistics.infrastructure.entity.ActionDB;
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
@Disabled("Outdated, actions do not exist")
public class ActionInternalControllerIT {

    @Autowired
    private Object actionInternalController;

    private static JdbcTemplate jdbcTemplate;
    private final LocalDate date = LocalDate.now();

    @BeforeAll
    public static void before() {
        jdbcTemplate = new JdbcTemplate(BlackJackService.getDataSource());
    }

    @AfterEach
    public void clearDatabase() {
        jdbcTemplate.update("DELETE FROM ACTION;");
        jdbcTemplate.update("DELETE FROM PILE;");
        jdbcTemplate.update("DELETE FROM GAME;");
    }

    @Test
    public void givenGame_whenPlayerHits_forTheFirstTime_thenAddHitActionInDatabase() {
        Long gameId = createGameInDatabase(date, GameState.ONGOING);
        Long playerId = 1L;

//        actionInternalController.hit(gameId, playerId);

        ActionDB action = getActionByGameAndPlayer(gameId, playerId);

        assertThat(action.hitCount(), is(1));
    }

    @Test
    public void givenGame_whenPlayerHits_forTheSecondTime_thenHitCountInDatabaseIncreases() {
        Long gameId = createGameInDatabase(date, GameState.ONGOING);
        Long playerId = 1L;
        Integer hitCount = 1;
        createActionInDatabase(gameId, playerId, hitCount);

//        actionInternalController.hit(gameId, playerId);

        ActionDB action = getActionByGameAndPlayer(gameId, playerId);

        assertThat(action.hitCount(), is(2));
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

    private void createActionInDatabase(Long gameId, Long playerId, Integer hitCount) {
        String sql = "INSERT INTO ACTION (ACTION_FK_GAME_ID, ACTION_PLAYER_ID, ACTION_HIT_COUNT) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                gameId,
                playerId,
                hitCount
        );
    }

    private ActionDB getActionByGameAndPlayer(Long gameId, Long playerId) {

        ActionDB actionDB = jdbcTemplate.queryForObject("SELECT * FROM ACTION WHERE ACTION_FK_GAME_ID = ? AND ACTION_PLAYER_ID = ?", new Object[]{gameId, playerId},
                (rs, rowNum) -> new ActionDB(
                        rs.getLong("ACTION_ID"),
                        rs.getLong("ACTION_FK_GAME_ID"),
                        rs.getLong("ACTION_PLAYER_ID"),
                        rs.getInt("ACTION_HIT_COUNT")
                ));

        return actionDB;
    }

}
