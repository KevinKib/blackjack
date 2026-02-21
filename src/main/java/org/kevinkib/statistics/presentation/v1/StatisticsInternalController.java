package org.kevinkib.statistics.presentation.v1;

import org.kevinkib.statistics.business.StatisticsService;

public class StatisticsInternalController {

    private final StatisticsService service;

    public StatisticsInternalController(StatisticsService service) {
        this.service = service;
    }

    public Integer getTotalNumberOfGames() {
        return service.getTotalNumberOfGames();
    }

}
