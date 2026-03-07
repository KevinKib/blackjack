package org.kevinkib.statistics.business;

import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.port.out.GameRepository;
import org.kevinkib.statistics.business.port.in.StatisticsUseCase;

import java.util.List;

public class StatisticsService implements StatisticsUseCase {

    private final GameRepository gameRepository;

    public StatisticsService(GameRepository gameRepository) {
        this.gameRepository = gameRepository;
    }

    public double getWinPercentage() {
        List<Game> games = retrieveGameList();

        long gameCount = games.size();
        long wonGameCount = games.stream().filter(Game::isWin).count();

        return percentage(wonGameCount, gameCount);
    }

    private double percentage(long wonGameCount, long gameCount) {
        return (double) wonGameCount / gameCount * 100;
    }

    private List<Game> retrieveGameList() {
        return gameRepository.getGames();
    }

}
