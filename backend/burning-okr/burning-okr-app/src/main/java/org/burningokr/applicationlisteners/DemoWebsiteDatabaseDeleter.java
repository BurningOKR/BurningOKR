package org.burningokr.applicationlisteners;

import java.time.LocalDateTime;
import java.util.Arrays;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

@Component
public class DemoWebsiteDatabaseDeleter {

  private final WebApplicationContext context;
  private final String[] ignoredRepositories =
      new String[] {
        "adminUserRepository",
        "localUserRepository",
        "configurationRepository",
        "frontendLoggerRepository",
        "OAuthClientDetailsRepository",
        "OAuthConfigurationRepository",
        "initStateRepository"
      };
  private final int rateInMinutes = 1;
  private LocalDateTime nextDeletionDate;

  @Autowired
  public DemoWebsiteDatabaseDeleter(WebApplicationContext context) {
    this.context = context;
    updateNextDeletionDate();
  }

  @Scheduled(fixedRate = rateInMinutes * 60 * 1000)
  public void deleteAll() {
    String[] beans = context.getBeanNamesForType(ExtendedRepository.class);
    for (String bean : beans) {
      if (!isRepositoryIgnored(bean)) {
        ExtendedRepository repository = (ExtendedRepository) context.getBean(bean);
        repository.deleteAll();
      }
    }
    updateNextDeletionDate();
  }

  public LocalDateTime getNextDeletionDate() {
    return nextDeletionDate;
  }

  private boolean isRepositoryIgnored(String repositoryName) {
    return Arrays.asList(ignoredRepositories).contains(repositoryName);
  }

  private void updateNextDeletionDate() {
    nextDeletionDate = LocalDateTime.now().plusMinutes(rateInMinutes);
  }
}
