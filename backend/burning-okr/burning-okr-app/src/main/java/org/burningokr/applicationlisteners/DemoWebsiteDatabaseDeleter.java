package org.burningokr.applicationlisteners;

import lombok.RequiredArgsConstructor;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class DemoWebsiteDatabaseDeleter {

  private final WebApplicationContext context;
  private final String[] ignoredRepositories = new String[] {"adminUserRepository", "localUserRepository"};
  private final int rate = 5000;

  @Scheduled(fixedRate = rate)
  public void deleteAll() {
    String[] beans = context.getBeanNamesForType(ExtendedRepository.class);
    for(String bean : beans) {
      if (!isRepositoryIgnored(bean)) {
        ExtendedRepository repository = (ExtendedRepository) context.getBean(bean);
        repository.deleteAll();
      }
    }
  }

  public LocalDate getNextDeletionDate() {
    return null;
  }

  private boolean isRepositoryIgnored(String repositoryName) {
    return Arrays.asList(ignoredRepositories).contains(repositoryName);
  }
}
