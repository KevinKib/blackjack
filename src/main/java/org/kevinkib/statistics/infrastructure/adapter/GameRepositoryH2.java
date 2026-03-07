package org.kevinkib.statistics.infrastructure.adapter;

import org.kevinkib.LegacyBlackJackService;
import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.port.out.GameRepository;
import org.kevinkib.statistics.infrastructure.entity.GameDB;
import org.kevinkib.statistics.infrastructure.entity.CardDB;
import org.kevinkib.statistics.infrastructure.mapper.GameMapper;
import org.kevinkib.statistics.infrastructure.mapper.HandMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Map;

public class GameRepositoryH2 implements GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameRepositoryH2() {
        this.jdbcTemplate = new JdbcTemplate(LegacyBlackJackService.getDataSource());
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
        List<CardDB> cardDBList = jdbcTemplate.query("SELECT * FROM PILE WHERE PILE_PLAYER_ID = ?", new Object[]{playerId},
                (rs, rowNum) -> new CardDB(
                        rs.getLong("PILE_ID"),
                        rs.getLong("PILE_FK_GAME_ID"),
                        rs.getInt("PILE_CARD_RANK"),
                        rs.getString("PILE_CARD_COLOR")
                ));

        return HandMapper.mapPilesFromDifferentGames(cardDBList);
    }

}