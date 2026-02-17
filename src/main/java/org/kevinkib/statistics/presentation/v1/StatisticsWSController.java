package org.kevinkib.statistics.presentation.v1;

import org.kevinkib.statistics.business.StatisticsService;
import org.kevinkib.statistics.business.model.StatisticsReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics/v1")
public class StatisticsWSController {

    @Autowired
    private StatisticsService statisticsService;

    @RequestMapping(value = "/report", produces = "application/json")
    public StatisticsReportResponse generateStatisticsReport() {
        StatisticsReport statisticsReport = statisticsService.generateStatisticsReport();

        return new StatisticsReportResponse(statisticsReport);
    }


}
