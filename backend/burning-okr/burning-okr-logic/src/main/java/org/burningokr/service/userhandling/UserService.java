package org.burningokr.service.userhandling;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.users.IUser;
import org.burningokr.model.users.User;
import org.burningokr.repositories.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  Collection<User> findAll() {
    return userRepository.findAll();
  }

  Collection<User> findAllActive() {
    return null;
  }

  Collection<User> findAllInactive() {
    return null;
  }

  User getCurrentUser() {
    return null;
  }

  IUser findById(UUID userId) {
    return null;
  }

  boolean doesUserExist(UUID userId) {
    return false;
  }
}
