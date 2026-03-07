package org.kevinkib.statistics.infrastructure.mapper;

import org.kevinkib.LegacyBlackJackService;
import org.kevinkib.cards.domain.Hand;
import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.model.GameOutcome;
import org.kevinkib.statistics.infrastructure.entity.GameDB;

import java.util.List;
import java.util.Map;

public class GameMapper {

    public static Game mapToDomain(GameDB gameDB) {
        GameOutcome outcome = GameOutcomeMapper.fromState(gameDB.state());

        int playerScore = LegacyBlackJackService.calculateScore(gameDB.id());
        int playerNbCards = LegacyBlackJackService.calculateNbCards(gameDB.id());

        return new Game(outcome, playerScore, playerNbCards);
    }

    public static List<Game> mapToDomain(List<GameDB> gameDB) {
        return gameDB.stream().map(GameMapper::mapToDomain).toList();
    }

}
