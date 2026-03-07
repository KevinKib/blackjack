package org.kevinkib.statistics.business.model;

public record Game(
        GameOutcome outcome,
        int playerScore,
        int playerNbCards) {

    public boolean isWin() {
        return GameOutcome.WIN.equals(outcome);
    }

    public boolean isBlackjack() {
        return playerScore == 21 && playerNbCards == 2;
    }

}
