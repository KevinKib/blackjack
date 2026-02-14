package org.kevinkib.statistics.business;

import java.time.LocalDate;

public record Game(Long id, Participant player, Participant dealer, GameResult result) {

}
