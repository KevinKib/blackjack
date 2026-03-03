package org.kevinkib.statistics.infrastructure.mapper;

import org.kevinkib.statistics.business.model.GameOutcome;

public class GameOutcomeMapper {

    public static GameOutcome fromState(String state) {
        return switch (state) {
            case "WIN" -> GameOutcome.WIN;
            case "TIE" -> GameOutcome.DRAW;
            case "LOSE" -> GameOutcome.LOSS;
            default -> null;
        };
    }

}
