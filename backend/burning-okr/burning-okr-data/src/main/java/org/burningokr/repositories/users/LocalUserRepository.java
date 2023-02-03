package org.burningokr.repositories.users;

import org.burningokr.model.users.LocalUser;
import org.burningokr.repositories.ExtendedRepository;

import java.util.Optional;
import java.util.UUID;

public interface LocalUserRepository extends ExtendedRepository<LocalUser, UUID> {
  Optional<LocalUser> findByMail(String mail);

  Iterable<LocalUser> findByActive(Boolean active);
}
