package org.kevinkib.config;

import org.kevinkib.statistics.business.ActionService;
import org.kevinkib.statistics.business.port.ActionPort;
import org.kevinkib.statistics.infrastructure.ActionRepository;
import org.kevinkib.statistics.presentation.ActionInternalController;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@SpringBootConfiguration
public class AppConfig {

    @Bean
    public ActionInternalController actionInternalController() {
        return new ActionInternalController(actionService());
    }

    @Bean
    public ActionService actionService() {
        return new ActionService(actionPort());
    }

    @Bean
    public ActionPort actionPort() {
        return new ActionRepository();
    }

}
