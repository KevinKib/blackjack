package org.kevinkib.statistics.business;

import org.kevinkib.statistics.business.port.ActionPort;

public class ActionService {

    private final ActionPort actionPort;

    public ActionService(ActionPort actionPort) {
        this.actionPort = actionPort;
    }

    public void hit(Long gameId, Long playerId) {

        Integer hitCount = actionPort.hitCount(gameId, playerId);

        if (hitCount != 0) {
            actionPort.edit(gameId, playerId, ++hitCount);
        }
        else {
            actionPort.create(gameId, playerId, 1);
        }

    }
}
