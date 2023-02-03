package org.burningokr.service.userhandling;

import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public interface UserService {
  Collection<User> findAll();

  Collection<User> findAllActive();

  Collection<User> findAllInactive();

  User getCurrentUser();

  User findById(UUID userId);

  boolean doesUserExist(UUID userId);
}
