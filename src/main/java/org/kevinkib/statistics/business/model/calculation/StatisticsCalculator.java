package org.kevinkib.statistics.business.model.calculation;

import org.kevinkib.statistics.business.model.game.Game;
import org.kevinkib.statistics.business.model.game.Hand;

import java.util.List;

import static org.kevinkib.statistics.business.model.game.GameOutcome.WIN;

public class StatisticsCalculator {

    public StatisticsReport getStatisticsReport(List<Game> games) {

        int nbGames = games.size();
        long nbWonGames = games.stream().filter(game -> WIN.equals(game.outcome())).count();
        double winRate = 0;

        if (nbGames > 0) {
            winRate = (double) nbWonGames / nbGames * 100;
        }

        List<Hand> hands = null;

        return new StatisticsReport(nbGames, winRate, getPlayerScoreStatistics(hands));
    }

    private ScoreStatistics getPlayerScoreStatistics(List<Hand> hands) {
        return null;
    }

}
