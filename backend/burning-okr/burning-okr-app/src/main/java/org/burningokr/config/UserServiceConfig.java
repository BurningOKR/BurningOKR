package org.burningokr.config;

import lombok.RequiredArgsConstructor;
import org.burningokr.repositories.users.AadUserRepository;
import org.burningokr.repositories.users.LocalUserRepository;
import org.burningokr.service.activity.ActivityService;
import org.burningokr.service.condition.AadCondition;
import org.burningokr.service.condition.LocalUserCondition;
import org.burningokr.service.userhandling.AadUserService;
import org.burningokr.service.userhandling.LocalUserService;
import org.burningokr.service.userhandling.PasswordService;
import org.burningokr.service.userhandling.UserService;
import org.burningokr.service.userutil.AadUserListUpdater;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Conditional;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

@Configuration
@RequiredArgsConstructor
public class UserServiceConfig {

  private final AadUserRepository aadUserRepository;
  @Nullable
  private final AadUserListUpdater aadUserListUpdater;
  private final LocalUserRepository localUserRepository;
  private final PasswordService passwordService;
  private final ActivityService activityService;

  @Bean
  @Conditional(AadCondition.class)
  public UserService getAadUserService() {
    return new AadUserService(aadUserRepository, aadUserListUpdater);
  }

  @Bean
  @Conditional(LocalUserCondition.class)
  public UserService getLocalService() {
    return new LocalUserService(localUserRepository, passwordService, activityService);
  }
}
