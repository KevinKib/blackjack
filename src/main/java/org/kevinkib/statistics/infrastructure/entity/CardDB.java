package org.kevinkib.statistics.infrastructure.entity;

public record CardDB(
        long id,
        long gameId,
        int cardRank,
        String cardColor
) {
}
