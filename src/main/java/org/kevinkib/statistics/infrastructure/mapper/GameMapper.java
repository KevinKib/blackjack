package org.kevinkib.statistics.infrastructure.mapper;

import org.kevinkib.statistics.business.model.game.Game;
import org.kevinkib.statistics.business.model.game.GameOutcome;
import org.kevinkib.statistics.business.model.game.Hand;
import org.kevinkib.statistics.infrastructure.entity.GameDB;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class GameMapper {

    public static Game mapToDomain(GameDB gameDB, Hand hand) {
        GameOutcome outcome = GameOutcomeMapper.fromState(gameDB.state());

        return new Game(outcome, hand);
    }

    public static List<Game> mapToDomain(List<GameDB> games, Map<Long, Hand> handsByGameId) {
        return games.stream()
                .map(gameDB -> mapToDomain(gameDB, handsByGameId.get(gameDB.id())))
                .toList();
    }

}
