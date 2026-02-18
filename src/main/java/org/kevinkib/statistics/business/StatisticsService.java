package org.kevinkib.statistics.business;

import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.model.StatisticsCalculator;
import org.kevinkib.statistics.business.model.StatisticsReport;
import org.kevinkib.statistics.business.port.GamePort;

import java.util.List;

public class StatisticsService {

    private final GamePort gamePort;

    public StatisticsService(GamePort gamePort) {
        this.gamePort = gamePort;
    }

    public StatisticsReport generateStatisticsReport() {
        List<Game> allGames = gamePort.getGames();
        return new StatisticsCalculator().calculate(allGames);
    }


}
