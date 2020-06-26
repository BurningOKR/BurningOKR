package org.burningokr.applicationlisteners;

import java.time.LocalDateTime;
import java.util.Arrays;

import org.burningokr.repositories.ExtendedRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

@Component
public class DemoWebsiteDatabaseDeleter {

  private final WebApplicationContext context;
  private final EntityManager manager;

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
  private final int rateInMinutes = 120;
  private LocalDateTime nextDeletionDate;

  @Autowired
  public DemoWebsiteDatabaseDeleter(WebApplicationContext context,
                                    EntityManagerFactory managerFactory) {
    this.manager = managerFactory.createEntityManager();
    this.context = context;
    updateNextDeletionDate();
    // fillDatabase();
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

  // TODO (R.J. 26.06.20) This is just an example on how to execute SQL Scripts in Spring
  // This should be moved into its own Service and read the SQL string from an SQL File.
  @Transactional
  public void fillDatabase() {
    EntityTransaction transaction = manager.getTransaction();
    transaction.begin();
    manager.createNativeQuery("INSERT INTO configuration (id, name, value, type) VALUES (1337, 'Test', 'Test', 'text');")
        .executeUpdate();
    transaction.commit();
  }
}
