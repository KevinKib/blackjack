package org.kevinkib.statistics.infrastructure.entity;

public record PileDB(
        Long id,
        Long gameId,
        Long playerId,
        Integer cardRank,
        String cardColor
) {
}
