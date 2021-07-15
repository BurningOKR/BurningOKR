package org.burningokr.repositories.users;

import java.util.UUID;
import org.burningokr.model.users.AuditorUser;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuditorUserRepository extends ExtendedRepository<AuditorUser, UUID> {}
