package org.kevinkib.statistics.infrastructure.entity;

public record PileDB(
        long id,
        long gameId,
        int cardRank,
        String cardColor
) {
}
