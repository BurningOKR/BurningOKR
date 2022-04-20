package org.burningokr.service.userhandling;

import java.util.Collection;
import java.util.UUID;
import org.burningokr.model.users.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
  Collection<User> findAll();

  Collection<User> findAllActive();

  User getCurrentUser();

  User findById(UUID userId);

  boolean doesUserExist(UUID userId);
}
