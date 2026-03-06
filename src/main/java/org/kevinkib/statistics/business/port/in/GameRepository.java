package org.kevinkib.statistics.business.port.in;

import org.kevinkib.statistics.business.model.Game;

import java.util.List;

public interface GameRepository {

    List<Game> getGames();

}
