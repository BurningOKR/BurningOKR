package org.burningokr.repositories.users;

import java.util.Optional;
import java.util.UUID;
import org.burningokr.model.users.PasswordToken;
import org.burningokr.repositories.ExtendedRepository;

public interface PasswordTokenRepository extends ExtendedRepository<PasswordToken, UUID> {
  Optional<PasswordToken> findByEmailIdentifier(UUID emailIdentifier);
}
