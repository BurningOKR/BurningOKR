package org.burningokr.repositories.users;

import java.util.UUID;
import org.burningokr.model.users.AadUser;
import org.burningokr.repositories.ExtendedRepository;

public interface AadUserRepository extends ExtendedRepository<AadUser, UUID> {
  AadUser findByMailNickname(String mailNickname);
}
