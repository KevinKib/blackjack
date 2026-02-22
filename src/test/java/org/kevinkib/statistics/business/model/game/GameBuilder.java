package org.kevinkib.statistics.business.model.game;

public final class GameBuilder {
    private GameOutcome outcome;
    private Hand hand;

    private GameBuilder() {
    }

    public static GameBuilder aGame() {
        return new GameBuilder();
    }

    public GameBuilder withOutcome(GameOutcome outcome) {
        this.outcome = outcome;
        return this;
    }

    public GameBuilder withPlayerHand(Hand hand) {
        this.hand = hand;
        return this;
    }

    public Game build() {
        return new Game(outcome, hand);
    }
}
