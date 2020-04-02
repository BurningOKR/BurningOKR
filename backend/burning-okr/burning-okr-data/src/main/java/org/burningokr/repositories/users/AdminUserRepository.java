package org.burningokr.repositories.users;

import java.util.UUID;
import org.burningokr.model.users.AdminUser;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminUserRepository extends ExtendedRepository<AdminUser, UUID> {}
