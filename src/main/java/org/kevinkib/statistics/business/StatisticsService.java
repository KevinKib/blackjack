package org.kevinkib.statistics.business;

import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.model.StatisticsReport;
import org.kevinkib.statistics.business.model.Hand;
import org.kevinkib.statistics.business.port.in.GameRepository;
import org.kevinkib.statistics.business.port.out.StatisticsUseCase;

import java.util.List;
import java.util.Objects;

public class StatisticsService implements StatisticsUseCase {

    private final GameRepository gameRepository;

    public StatisticsService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public StatisticsReport getStatisticsReport() {
        List<Game> games = gameRepository.getGames();
        List<Hand> hands = games.stream().map(Game::playerHand).filter(Objects::nonNull).toList();

        if (games.isEmpty() || hands.isEmpty()) {
            return StatisticsReport.empty();
        }

        double winRate = computeWinRate(games);
        double average = computeAverageScore(hands);
        double bustRate = computeBustRate(hands);
        double blackJackRate = computeBlackJackRate(hands);

        return new StatisticsReport(winRate, average, bustRate, blackJackRate);
    }

    private double computeWinRate(List<Game> games) {
        int nbGames = games.size();

        long nbWonGames = games.stream().filter(Game::isWin).count();
        return percentage(nbWonGames, nbGames);
    }

    private double computeBlackJackRate(List<Hand> hands) {
        int nbHands = hands.size();

        long nbBlackJacks = hands.stream().filter(Hand::isBlackjack).count();
        return percentage(nbBlackJacks, nbHands);
    }

    private double computeBustRate(List<Hand> hands) {
        int nbHands = hands.size();

        long nbBusts = hands.stream().filter(Hand::isBusted).count();
        return percentage(nbBusts, nbHands);
    }

    private double computeAverageScore(List<Hand> hands) {
        int nbHands = hands.size();

        int sumOfScores = hands.stream().map(Hand::score).reduce(0, Integer::sum);
        return (double) sumOfScores / nbHands;
    }

    private double percentage(long nbWonGames, long nbGames) {
        return (double) nbWonGames / nbGames * 100;
    }

}
