package org.kevinkib.statistics.presentation.v1.mapper;

import org.kevinkib.statistics.business.model.StatisticsReport;
import org.kevinkib.statistics.presentation.v1.dto.StatisticsReportDto;

public class StatisticsReportDtoMapper {

    public static StatisticsReportDto mapFromDomain(StatisticsReport report) {
        return new StatisticsReportDto(
                report.nbGames(),
                report.winRate());
    }
}
