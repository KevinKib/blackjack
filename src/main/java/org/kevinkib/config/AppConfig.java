package org.kevinkib.config;

import org.kevinkib.statistics.business.StatisticsService;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class AppConfig {

    @Bean
    public StatisticsService statisticsService() {
        return new StatisticsService(null);
    }

}
