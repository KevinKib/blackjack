package org.kevinkib.statistics.presentation.v1;

import org.kevinkib.statistics.business.StatisticsService;
import org.kevinkib.statistics.presentation.v1.dto.StatisticsReportDto;

public class StatisticsInternalController {

    private final StatisticsService service;

    public StatisticsInternalController(StatisticsService service) {
        this.service = service;
    }

    public StatisticsReportDto getStatisticsReport() {
        return null;
    }

}
