package org.burningokr.repositories.users;

import org.burningokr.model.users.AdminUser;
import org.burningokr.repositories.ExtendedRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AdminUserRepository extends ExtendedRepository<AdminUser, UUID> {
}
