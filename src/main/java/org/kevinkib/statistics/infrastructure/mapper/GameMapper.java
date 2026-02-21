package org.kevinkib.statistics.infrastructure.mapper;

import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.model.GameOutcome;
import org.kevinkib.statistics.infrastructure.entity.GameDB;

import java.util.List;
import java.util.Objects;

public class GameMapper {

    public static Game mapToDomain(GameDB gameDB) {
        GameOutcome outcome = GameOutcomeMapper.fromState(gameDB.state());

        if (outcome == null) {
            return null;
        }

        return new Game(outcome);
    }

    public static List<Game> mapToDomain(List<GameDB> games) {
        return games.stream()
                .map(GameMapper::mapToDomain)
                .filter(Objects::nonNull)
                .toList();
    }

}
