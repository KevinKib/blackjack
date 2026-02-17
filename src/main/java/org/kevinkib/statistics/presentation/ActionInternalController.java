package org.kevinkib.statistics.presentation;

import org.kevinkib.statistics.business.ActionService;

public class ActionInternalController {

    private final ActionService actionService;

    public ActionInternalController(ActionService actionService) {
        this.actionService = actionService;
    }

    public void hit(Long gameId, Long playerId) {
        actionService.hit(gameId, playerId);
    }

}
