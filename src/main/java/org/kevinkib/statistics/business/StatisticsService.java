package org.kevinkib.statistics.business;

import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.model.StatisticsCalculator;
import org.kevinkib.statistics.business.model.StatisticsReport;
import org.kevinkib.statistics.business.port.GameRepository;

import java.util.List;

public class StatisticsService {

    private final GameRepository gameRepository;

    public StatisticsService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public StatisticsReport getStatisticsReport() {
        List<Game> games = gameRepository.getGames();
        return new StatisticsCalculator().getStatisticsReport(games);
    }

}
