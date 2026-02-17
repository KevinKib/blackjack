package org.kevinkib.statistics.business.port;

public interface ActionPort {

    void create(Long gameId, Long playerId, Integer hitCount);
    void edit(Long gameId, Long playerId, Integer hitCount);
    Integer hitCount(Long gameId, Long playerId);

}
