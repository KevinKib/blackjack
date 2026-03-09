package org.kevinkib.statistics.business.model;

public record Game(
        GameOutcome outcome,
        int playerScore,
        int playerNbCards) {

    public boolean isWin() {
        return GameOutcome.WIN.equals(outcome);
    }



}
