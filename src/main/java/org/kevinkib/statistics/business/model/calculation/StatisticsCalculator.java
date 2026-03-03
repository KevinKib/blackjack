package org.kevinkib.statistics.business.model.calculation;

import org.kevinkib.statistics.business.model.game.Game;
import org.kevinkib.statistics.business.model.game.Hand;

import java.util.List;
import java.util.Objects;

import static org.kevinkib.statistics.business.model.game.GameOutcome.WIN;

public class StatisticsCalculator {

    public StatisticsReport getStatisticsReport(List<Game> games) {
        int nbGames = games.size();

        List<Hand> hands = games.stream().map(Game::playerHand).filter(Objects::nonNull).toList();
        int nbHands = hands.size();

        if (nbGames == 0 || nbHands == 0) {
            return StatisticsReport.empty();
        }

        long nbWonGames = games.stream().filter(Game::isWin).count();
        double winRate = percentage(nbWonGames, nbGames);

        int sumOfScores = hands.stream().map(Hand::score).reduce(0, Integer::sum);
        double average = (double) sumOfScores / nbHands;

        long nbBusts = hands.stream().filter(Hand::isBusted).count();
        double bustRate = percentage(nbBusts, nbHands);

        long nbBlackJacks = hands.stream().filter(Hand::isBlackjack).count();
        double blackJackRate = percentage(nbBlackJacks, nbHands);

        return new StatisticsReport(winRate, average, bustRate, blackJackRate);
    }

    private double percentage(long nbWonGames, long nbGames) {
        return (double) nbWonGames / nbGames * 100;
    }

}
