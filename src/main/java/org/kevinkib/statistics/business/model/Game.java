package org.kevinkib.statistics.business.model;

public record Game(
        GameOutcome outcome,
        Hand playerHand) {

    public boolean isWin() {
        return GameOutcome.WIN.equals(outcome);
    }

}
