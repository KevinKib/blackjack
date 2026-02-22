package org.kevinkib.statistics.infrastructure.mapper;

import org.junit.jupiter.api.Test;
import org.kevinkib.statistics.business.model.game.Game;
import org.kevinkib.statistics.business.model.game.GameOutcome;
import org.kevinkib.statistics.infrastructure.entity.GameDB;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.junit.MatcherAssert.assertThat;

public class GameMapperTest {

    @Test
    public void givenOngoingGameDB_thenMapToNull() {
        GameDB gameDB = new GameDB(null, null, "ONGOING");

        assertThat(GameMapper.mapToDomain(gameDB), is(nullValue()));
    }

    @Test
    public void givenTiedGameDB_thenMapOutcomeToCorrectState() {
        GameDB gameDB = new GameDB(null, null, "TIE");

        Game game = GameMapper.mapToDomain(gameDB);
        assert game != null;
        assertThat(game.outcome(), is(GameOutcome.DRAW));
    }

}
