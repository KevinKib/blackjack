package org.kevinkib.config;

import org.kevinkib.statistics.business.StatisticsService;
import org.kevinkib.statistics.business.port.GameRepository;
import org.kevinkib.statistics.infrastructure.adapter.GameRepositoryH2;
import org.kevinkib.statistics.presentation.v1.StatisticsInternalController;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AppConfig {

    @Bean
    public StatisticsInternalController statisticsInternalController() {
        return new StatisticsInternalController(statisticsService());
    }

    @Bean
    public StatisticsService statisticsService() {
        return new StatisticsService(gameRepository());
    }

    @Bean
    public GameRepository gameRepository() {
        return new GameRepositoryH2();
    }

}