package org.burningokr.repositories.settings;

import org.burningokr.model.settings.UserSettings;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserSettingsRepository extends ExtendedRepository<UserSettings, Long> {
  @Query("SELECT s FROM UserSettings s WHERE s.userId = ?1")
  UserSettings findUserSettingsByUserId(UUID userId);
}
