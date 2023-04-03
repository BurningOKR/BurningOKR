package org.burningokr.service.userhandling;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.burningokr.model.users.User;
import org.burningokr.repositories.users.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  public List<User> findAll() {
    return userRepository.findAll();
  }

  public List<User> findAllActive() {
    return userRepository.findAllByActiveIsTrue();
  }

  public List<User> findAllInactive() {
    return userRepository.findAllByActiveIsFalse();
  }

  public User updateUser(User user) {
    return userRepository.save(user);
  }

  public User getCurrentUser() { // TODO remove -> replaced
    throw new NotImplementedException("getCurrentUser in UserService:34");
  }

  public Optional<User> findById(UUID userId) {
    return userRepository.findById(userId);
  }
}
