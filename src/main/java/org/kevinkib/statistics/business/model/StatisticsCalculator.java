package org.kevinkib.statistics.business.model;

import java.util.List;

import static org.kevinkib.statistics.business.model.GameOutcome.WIN;

public class StatisticsCalculator {

    public StatisticsReport getStatisticsReport(List<Game> games) {

        int nbGames = games.size();
        long nbWonGames = games.stream().filter(game -> WIN.equals(game.outcome())).count();
        double winRate = 0;

        if (nbGames > 0) {
            winRate = (double) nbWonGames / nbGames * 100;
        }

        return new StatisticsReport(nbGames, winRate);
    }

}
