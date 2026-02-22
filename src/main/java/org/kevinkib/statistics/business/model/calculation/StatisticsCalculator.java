package org.kevinkib.statistics.business.model.calculation;

import org.kevinkib.statistics.business.model.game.Game;
import org.kevinkib.statistics.business.model.game.Hand;

import java.util.List;
import java.util.Objects;

import static org.kevinkib.statistics.business.model.game.GameOutcome.WIN;

public class StatisticsCalculator {

    public StatisticsReport getStatisticsReport(List<Game> games) {
        int nbGames = games.size();
        if (nbGames == 0) {
            return StatisticsReport.empty();
        }

        long nbWonGames = games.stream().filter(game -> WIN.equals(game.outcome())).count();
        double winRate = (double) nbWonGames / nbGames * 100;

        List<Hand> hands = games.stream().map(Game::playerHand).filter(Objects::nonNull).toList();

        return new StatisticsReport(nbGames, winRate, getPlayerScoreStatistics(hands));
    }

    private ScoreStatistics getPlayerScoreStatistics(List<Hand> hands) {
        int nbHands = hands.size();

        if (nbHands == 0) {
            return ScoreStatistics.empty();
        }

        int sumOfScores = hands.stream().map(Hand::score).reduce(0, Integer::sum);
        double average = (double) sumOfScores / nbHands;

        long nbBusts = hands.stream().filter(Hand::isBusted).count();
        double bustRate = (double) nbBusts / nbHands;

        long nbBlackJacks = hands.stream().filter(Hand::isBlackjack).count();
        double blackJackRate = (double) nbBlackJacks / nbHands;

        return new ScoreStatistics(average, bustRate, blackJackRate);
    }

}
