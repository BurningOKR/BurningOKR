package org.burningokr.applicationlisteners;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.configuration.OAuthConfiguration;
import org.burningokr.model.configuration.OAuthConfigurationName;
import org.burningokr.service.EnvironmentService;
import org.burningokr.service.configuration.OAuthConfigurationService;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ApplicationStartupDemoConfigurer {

    private final OAuthConfigurationService oAuthConfigurationService;

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationEvent(ApplicationReadyEvent event) {
        oAuthConfigurationService.setOAuthConfiguration(
                OAuthConfigurationName.AUTH_TYPE, EnvironmentService.authModeDemo);
    }

}
