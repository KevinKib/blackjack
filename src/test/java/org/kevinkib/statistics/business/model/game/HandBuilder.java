package org.kevinkib.statistics.business.model.game;

public final class HandBuilder {
    private int score;
    private int nbCards;

    private HandBuilder() {
    }

    public static HandBuilder aHand() {
        return new HandBuilder();
    }

    public HandBuilder withScore(int score) {
        this.score = score;
        return this;
    }

    public HandBuilder withNbCards(int nbCards) {
        this.nbCards = nbCards;
        return this;
    }

    public Hand build() {
        return new Hand(score, nbCards);
    }
}
