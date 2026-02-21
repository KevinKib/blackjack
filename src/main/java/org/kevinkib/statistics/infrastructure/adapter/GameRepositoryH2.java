package org.kevinkib.statistics.infrastructure.adapter;

import org.kevinkib.BlackJackService;
import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.port.GameRepository;
import org.kevinkib.statistics.infrastructure.entity.GameDB;
import org.kevinkib.statistics.infrastructure.mapper.GameMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

public class GameRepositoryH2 implements GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameRepositoryH2() {
        this.jdbcTemplate = new JdbcTemplate(BlackJackService.getDataSource());
    }

    @Override
    public List<Game> getGames() {
        List<GameDB> gameDBList = jdbcTemplate.query("SELECT * FROM GAME", new Object[]{},
                (rs, rowNum) -> new GameDB(
                        rs.getLong("GAME_ID"),
                        rs.getDate("GAME_CREATION_DATE"),
                        rs.getString("GAME_STATE")
                ));

        if (gameDBList.isEmpty()) {
            return Collections.emptyList();
        }

        return GameMapper.mapToDomain(gameDBList);
    }

}