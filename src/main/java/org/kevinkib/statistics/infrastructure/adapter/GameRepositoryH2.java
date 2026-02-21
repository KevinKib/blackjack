package org.kevinkib.statistics.infrastructure.adapter;

import org.kevinkib.BlackJackService;
import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.port.GameRepository;
import org.kevinkib.statistics.infrastructure.entity.GameDB;
import org.kevinkib.statistics.infrastructure.mapper.GameMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Collections;
import java.util.List;

public class GameRepositoryH2 implements GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameRepositoryH2() {
        this.jdbcTemplate = new JdbcTemplate(BlackJackService.getDataSource());
    }

}