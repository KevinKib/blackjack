package org.kevinkib.statistics.business.model.game;

public record Game(
        GameOutcome outcome,
        Hand playerHand) {

    public boolean isWin() {
        return GameOutcome.WIN.equals(outcome);
    }

}
