package org.kevinkib.statistics.infrastructure.mapper;

import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.infrastructure.entity.GameDB;

import java.util.List;

public class GameMapper {

    public static Game mapToDomain(GameDB game) {
        return new Game();
    }

    public static List<Game> mapToDomain(List<GameDB> games) {
        return games.stream().map(GameMapper::mapToDomain).toList();
    }

}
