package org.kevinkib.statistics.business.model;

public record Hand(int score, int nbCards) {

    public static final int HAND_MAXIMUM_SCORE = 21;

    public boolean isBlackjack() {
        return score == HAND_MAXIMUM_SCORE && nbCards == 2;
    }

    public boolean isBusted() {
        return score > HAND_MAXIMUM_SCORE;
    }

}
