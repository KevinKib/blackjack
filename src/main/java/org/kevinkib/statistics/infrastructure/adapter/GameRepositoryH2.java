package org.kevinkib.statistics.infrastructure.adapter;

import org.kevinkib.BlackJackService;
import org.kevinkib.statistics.business.port.in.GameRepository;
import org.springframework.jdbc.core.JdbcTemplate;

public class GameRepositoryH2 implements GameRepository {

    private final JdbcTemplate jdbcTemplate;

    public GameRepositoryH2() {
        this.jdbcTemplate = new JdbcTemplate(BlackJackService.getDataSource());
    }

}