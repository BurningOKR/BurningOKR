package org.burningokr.config;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationInfoService;
import org.flywaydb.core.api.MigrationVersion;
import org.springframework.boot.autoconfigure.flyway.FlywayMigrationInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

@Configuration
public class MigrationConfiguration {

  /** Override default flyway initializer to do nothing. */
  @Bean
  FlywayMigrationInitializer flywayInitializer(Flyway flyway) {
    MigrationInfo version = flyway.info().current();
    //flyway.setBaselineVersionAsString("52");
    return new FlywayMigrationInitializer(flyway, (f) -> {});
  }

  /** Create a second flyway initializer to run after jpa has created the schema. */
  @Bean
  @DependsOn("entityManagerFactory")
  FlywayMigrationInitializer delayedFlywayInitializer(Flyway flyway) {
    return new FlywayMigrationInitializer(flyway, null);
  }
}
