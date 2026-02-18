package org.kevinkib;

public enum GameState {

    WIN, TIE, LOSE, ONGOING;

    public static GameState from(String stateName) {
        for (GameState state : GameState.values()) {
            if (state.name().equals(stateName)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid state name: "+stateName);
    }

}
