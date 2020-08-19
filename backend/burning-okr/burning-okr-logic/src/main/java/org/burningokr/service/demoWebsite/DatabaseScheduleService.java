package org.burningokr.service.demoWebsite;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DatabaseScheduleService {

  @Value("classpath:demoWebsite/DemoWebsiteDefaultData.sql")
  private Resource sqlFile;

  private final EntityManagerFactory entityManagerFactory;

  private final int RATE_IN_MINUTES = 120;
  private LocalDateTime nextSchedule = LocalDateTime.now().plusMinutes(RATE_IN_MINUTES);

  @Scheduled(fixedRate = RATE_IN_MINUTES * 60 * 1000)
  public void scheduledDeletion() {
    String query = getSQLQueries();
    executeSQL(query);
    updateSchedule();
  }

  private void updateSchedule() {
    nextSchedule = LocalDateTime.now().plusMinutes(RATE_IN_MINUTES);
  }

  private String getSQLQueries() {
    String content;

    try {
      Path path = Paths.get(sqlFile.getURI());
      List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
      content = String.join(System.lineSeparator(), lines);
    } catch (Exception e) {
      content = "";
    }

    return content;
  }

  @Transactional
  public void executeSQL(String sql) {
    EntityManager entityManager = entityManagerFactory.createEntityManager();
    EntityTransaction transaction = entityManager.getTransaction();
    transaction.begin();
    entityManager.createNativeQuery(sql).executeUpdate();
    transaction.commit();
  }

  public long getNextSchedule() {
    return Duration.between(LocalDateTime.now(), this.nextSchedule).toMillis();
  }

}
