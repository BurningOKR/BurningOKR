//package org.burningokr.config;
//
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.burningokr.model.configuration.DeprecatedMigrationChecksum;
//import org.flywaydb.core.Flyway;
//import org.flywaydb.core.api.MigrationInfo;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.boot.autoconfigure.flyway.FlywayMigrationStrategy;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.Resource;
//
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//
//@Configuration
//public class MigrationConfiguration {
//
//  private final Logger logger = LoggerFactory.getLogger(MigrationConfiguration.class);
//
//  @Value("classpath:db/deprecated-checksums.json")
//  Resource deprecatedChecksumsJson;
//
//  /**
//   * Override default flyway initializer to do nothing.
//   */
//  @Bean
//  public FlywayMigrationStrategy flywayMigrationStrategy() {
//    return flyway -> {
//      // do nothing
//    };
//  }
//
//  /*  */
//
//  /**
//   * Create a second flyway initializer to run after jpa has created the schema.
//   *//*
//  @Bean
//  @DependsOn("entityManagerFactory")
//  public FlywayMigrationStrategy delayedFlywayMigrationStrategy() {
//    return flyway -> {
//      // do nothing
//    };
//  }*/
//  private void safeRepairDatabase(Flyway flyway) {
//    MigrationInfo[] appliedMigrations = flyway.info().applied();
//    if (doesContainDeprecatedMigrations(appliedMigrations)) {
//      logger.info("Detected deprecated migration checksums. Updating migration checksums...");
//      flyway.repair();
//    }
//  }
//
//  private List<DeprecatedMigrationChecksum> getDeprecatedMigrationChecksums() {
//    ObjectMapper mapper = new ObjectMapper();
//    TypeReference<List<DeprecatedMigrationChecksum>> typeReference =
//      new TypeReference<List<DeprecatedMigrationChecksum>>() {
//      };
//    List<DeprecatedMigrationChecksum> deprecatedMigrationChecksums;
//    try {
//      deprecatedMigrationChecksums =
//        mapper.readValue(deprecatedChecksumsJson.getFile(), typeReference);
//    } catch (IOException e) {
//      // return an empty list, because the file does not exist.
//      return new ArrayList<>();
//    }
//    return deprecatedMigrationChecksums;
//  }
//
//  private boolean doesContainDeprecatedMigrations(MigrationInfo[] infos) {
//    List<DeprecatedMigrationChecksum> deprecatedMigrationChecksums =
//      getDeprecatedMigrationChecksums();
//
//    return Arrays.stream(infos)
//      .anyMatch(
//        migrationInfo ->
//          deprecatedMigrationChecksums.stream()
//            .anyMatch(
//              deprecatedMigrationChecksum ->
//                migrationInfo
//                  .getVersion()
//                  .getVersion()
//                  .equals(deprecatedMigrationChecksum.getVersion())
//                  && migrationInfo
//                  .getChecksum()
//                  .equals(deprecatedMigrationChecksum.getOldChecksum())));
//  }
//}
