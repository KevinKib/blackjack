package org.kevinkib.config;

import org.kevinkib.LegacyBlackJackService;
import org.kevinkib.cards.domain.french.FrenchDeckFactory;
import org.kevinkib.statistics.business.StatisticsService;
import org.kevinkib.statistics.business.port.out.GameRepository;
import org.kevinkib.statistics.infrastructure.adapter.GameRepositoryH2;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AppConfig {

    @Bean
    public LegacyBlackJackService legacyBlackJackService() {
        return new LegacyBlackJackService(
                LegacyBlackJackService.getDataSource(),
                new FrenchDeckFactory()
        );
    }

}