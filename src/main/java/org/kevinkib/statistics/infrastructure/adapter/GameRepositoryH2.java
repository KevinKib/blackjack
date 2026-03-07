package org.kevinkib.statistics.infrastructure.adapter;

import org.kevinkib.LegacyBlackJackService;
import org.kevinkib.statistics.business.model.Game;
import org.kevinkib.statistics.business.port.out.GameRepository;
import org.kevinkib.statistics.infrastructure.entity.GameDB;
import org.kevinkib.statistics.infrastructure.mapper.GameMapper;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;

public class GameRepositoryH2 implements GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameRepositoryH2() {
        this.jdbcTemplate = new JdbcTemplate(LegacyBlackJackService.getDataSource());
    }



}