package org.kevinkib.statistics.business;

import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.model.StatisticsReport;
import org.kevinkib.statistics.business.port.out.GameRepository;
import org.kevinkib.statistics.business.port.in.StatisticsUseCase;

import java.util.List;

public class StatisticsService implements StatisticsUseCase {

    private final GameRepository gameRepository;

    public StatisticsService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public StatisticsReport getStatisticsReport() {
        List<Game> games = retrieveGameList();

        double winRate = computeWinRate(games);
        double blackJackRate = computeBlackJackRate(games);

        return new StatisticsReport(winRate, blackJackRate);
    }

    private List<Game> retrieveGameList() {
        return gameRepository.getGames();
    }

    private double computeWinRate(List<Game> games) {
        long nbWonGames = games.stream().filter(Game::isWin).count();
        return percentage(nbWonGames, games.size());
    }

    private double computeBlackJackRate(List<Game> games) {
        long nbBlackJacks = games.stream().filter(Game::isBlackjack).count();
        return percentage(nbBlackJacks, games.size());
    }

    private double percentage(long nbWonGames, long nbGames) {
        if (nbGames == 0) {
            return 0.0;
        }

        return (double) nbWonGames / nbGames * 100;
    }

}
