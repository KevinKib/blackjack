package org.kevinkib.statistics.business.model;

public record Game(
        GameOutcome outcome) {

    public boolean isWin() {
        return GameOutcome.WIN.equals(outcome);
    }
}
