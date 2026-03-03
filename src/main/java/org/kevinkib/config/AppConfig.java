package org.kevinkib.config;

import org.kevinkib.BlackJackService;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.statistics.business.StatisticsService;
import org.kevinkib.statistics.business.port.in.GameRepository;
import org.kevinkib.statistics.business.port.out.StatisticsUseCase;
import org.kevinkib.statistics.infrastructure.adapter.GameRepositoryH2;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AppConfig {

    @Bean
    public BlackJackService blackJackService() {
        return new BlackJackService(BlackJackService.getDataSource(), new FrenchDeckFactory(), statisticsService());
    }

    @Bean
    public StatisticsUseCase statisticsService() {
        return new StatisticsService(gameRepository());
    }

    @Bean
    public GameRepository gameRepository() {
        return new GameRepositoryH2();
    }

}