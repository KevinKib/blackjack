package org.kevinkib.statistics.business.port.in;

import org.kevinkib.statistics.business.model.game.Game;

import java.util.List;

public interface GameRepository {

    List<Game> getGames();

}
