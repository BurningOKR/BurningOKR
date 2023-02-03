package org.burningokr.repositories.users;

import org.burningokr.model.users.AadUser;
import org.burningokr.repositories.ExtendedRepository;

import java.util.UUID;

public interface AadUserRepository extends ExtendedRepository<AadUser, UUID> {
  AadUser findByMailNickname(String mailNickname);
}
