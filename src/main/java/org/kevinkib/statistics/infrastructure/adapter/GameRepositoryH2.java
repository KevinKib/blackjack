package org.kevinkib.statistics.infrastructure.adapter;

import org.kevinkib.BlackJackService;
import org.kevinkib.statistics.business.model.game.Game;
import org.kevinkib.statistics.business.model.game.Hand;
import org.kevinkib.statistics.business.port.in.GameRepository;
import org.kevinkib.statistics.infrastructure.entity.GameDB;
import org.kevinkib.statistics.infrastructure.entity.PileDB;
import org.kevinkib.statistics.infrastructure.mapper.GameMapper;
import org.kevinkib.statistics.infrastructure.mapper.HandMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

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
                        rs.getString("GAME_STATE")
                ));

        Map<Long, Hand> handsByGameId = getPlayerHands();

        return GameMapper.mapToDomain(gameDBList, handsByGameId);
    }

    private Map<Long, Hand> getPlayerHands() {
        long playerId = 1;
        List<PileDB> pileDBList = jdbcTemplate.query("SELECT * FROM PILE WHERE PILE_PLAYER_ID = ?", new Object[]{playerId},
                (rs, rowNum) -> new PileDB(
                        rs.getLong("PILE_ID"),
                        rs.getLong("PILE_FK_GAME_ID"),
                        rs.getInt("PILE_CARD_RANK"),
                        rs.getString("PILE_CARD_COLOR")
                ));

        return HandMapper.mapPilesFromDifferentGames(pileDBList);
    }

}