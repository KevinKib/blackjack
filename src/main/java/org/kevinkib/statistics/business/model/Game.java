package org.kevinkib.statistics.business.model;

public record Game(
        Long id,
        Participant player,
        Participant dealer,
        GameResult result) {

}
