package org.burningokr.repositories.users;

import org.burningokr.model.users.AuditorUser;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AuditorUserRepository extends ExtendedRepository<AuditorUser, UUID> {
}
