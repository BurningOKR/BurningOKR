package org.burningokr.repositories.users;

import org.burningokr.model.users.PasswordToken;
import org.burningokr.repositories.ExtendedRepository;

import java.util.Optional;
import java.util.UUID;

public interface PasswordTokenRepository extends ExtendedRepository<PasswordToken, UUID> {
  Optional<PasswordToken> findByEmailIdentifier(UUID emailIdentifier);
}
