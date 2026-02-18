package org.kevinkib.statistics.infrastructure.mapper;

import org.kevinkib.statistics.business.model.GameResult;

public class GameResultMapper {

    public static GameResult apply(String gameStateDB) {
        return GameResult.WIN;
    }

}
