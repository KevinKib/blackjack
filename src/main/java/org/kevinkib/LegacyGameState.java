package org.kevinkib;

public enum LegacyGameState {

    WIN, TIE, LOSE, ONGOING;

    public static LegacyGameState from(String stateName) {
        for (LegacyGameState state : LegacyGameState.values()) {
            if (state.name().equals(stateName)) {
                return state;
            }
        }
        throw new IllegalArgumentException("Invalid state name: "+stateName);
    }

}
