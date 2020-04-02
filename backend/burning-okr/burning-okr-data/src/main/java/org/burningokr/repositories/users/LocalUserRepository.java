package org.burningokr.repositories.users;

import java.util.Optional;
import java.util.UUID;
import org.burningokr.model.users.LocalUser;
import org.burningokr.repositories.ExtendedRepository;

public interface LocalUserRepository extends ExtendedRepository<LocalUser, UUID> {
  Optional<LocalUser> findByMail(String mail);
}
