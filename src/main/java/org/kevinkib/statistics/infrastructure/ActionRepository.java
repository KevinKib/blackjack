package org.kevinkib.statistics.infrastructure;

import org.kevinkib.BlackJackService;
import org.kevinkib.statistics.business.port.ActionPort;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;

public class ActionRepository implements ActionPort {

    private final JdbcTemplate jdbcTemplate;

    public ActionRepository() {
        this.jdbcTemplate = new JdbcTemplate(BlackJackService.getDataSource());
    }

    @Override
    public void create(Long gameId, Long playerId, Integer hitCount) {
        String sql = "INSERT INTO ACTION (ACTION_FK_GAME_ID, ACTION_PLAYER_ID, ACTION_HIT_COUNT) VALUES (?, ?, ?)";

        jdbcTemplate.update(sql,
                gameId,
                playerId,
                hitCount
        );
    }

    @Override
    public void edit(Long gameId, Long playerId, Integer hitCount) {
        String sql = "UPDATE ACTION SET ACTION_HIT_COUNT = ? WHERE ACTION_FK_GAME_ID = ? AND ACTION_PLAYER_ID = ?";

        jdbcTemplate.update(sql,
                hitCount,
                gameId,
                playerId
        );
    }

    public Integer hitCount(Long gameId, Long playerId) {
        String sql = "SELECT ACTION_HIT_COUNT FROM ACTION WHERE ACTION_FK_GAME_ID = ? AND ACTION_PLAYER_ID = ?";

        try {
            Integer hitCount = jdbcTemplate.queryForObject(sql,
                    Integer.class,
                    gameId,
                    playerId
            );

            return hitCount;
        } catch (EmptyResultDataAccessException e) {
            return 0;
        }
    }

}
