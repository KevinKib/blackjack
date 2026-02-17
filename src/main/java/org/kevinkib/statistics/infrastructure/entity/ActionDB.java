package org.kevinkib.statistics.infrastructure.entity;

public record ActionDB(
        Long id,
        Long gameId,
        Long playerId,
        Integer hitCount
) {
}
