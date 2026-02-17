package org.kevinkib.statistics.infrastructure;

import org.kevinkib.BlackJackService;
import org.kevinkib.GameEntity;
import org.kevinkib.PileEntity;
import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.port.GamePort;
import org.kevinkib.statistics.infrastructure.entity.GameDB;
import org.kevinkib.statistics.infrastructure.entity.PileDB;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

public class GameRepository implements GamePort {

    private final JdbcTemplate jdbcTemplate;

    public GameRepository() {
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

        List<PileDB> pileDBList = jdbcTemplate.query("SELECT * FROM PILE", new Object[]{},
                (rs, rowNum) -> new PileDB(
                        rs.getLong("PILE_ID"),
                        rs.getLong("PILE_FK_GAME_ID"),
                        rs.getLong("PILE_PLAYER_ID"),
                        rs.getInt("PILE_CARD_RANK"),
                        rs.getString("PILE_CARD_COLOR")
                ));

        if (pileDBList.isEmpty()) {
            return Collections.emptyList();
        }


        for (GameDB gameDB : gameDBList) {
            new Game(
                    gameDB.id(),
                    null,
                    null,
                    null

            );
        }

        return Collections.emptyList();
    }
}
