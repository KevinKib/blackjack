package org.kevinkib.statistics.business.model;

public final class GameBuilder {
    private GameOutcome outcome;

    private GameBuilder() {
    }

    public static GameBuilder aGame() {
        return new GameBuilder();
    }

    public GameBuilder withOutcome(GameOutcome outcome) {
        this.outcome = outcome;
        return this;
    }

    public Game build() {
        return new Game(outcome);
    }
}
