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

        return winRate;
    }

    private double computeWinRate(List<Game> games) {
        long nbWonGames = games.stream().filter(Game::isWin).count();
        return percentage(nbWonGames, games.size());
    }

    private List<Game> retrieveGameList() {
        return gameRepository.getGames();
    }

    private double percentage(long nbWonGames, long nbGames) {
        if (nbGames == 0) {
            return 0.0;
        }

        return (double) nbWonGames / nbGames * 100;
    }

}
