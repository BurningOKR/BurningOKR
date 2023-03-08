package org.burningokr.service.userhandling;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.burningokr.model.users.User;
import org.burningokr.repositories.users.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService implements UserDetailsService {

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

  public User getCurrentUser() { // TODO implement
    throw new NotImplementedException("getCurrentUser in UserService:34");
  }

  public User findById(UUID userId) throws EntityNotFoundException {
    var databaseUser = userRepository.findById(userId);
    if (databaseUser.isPresent()) {
      return databaseUser.get();
    } else {
      log.warn("entity with uuid: %s not found".formatted(userId));
      throw new EntityNotFoundException();
    }
  }

  // Username is the UUID of the User
  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info(username);
    var user = findById(UUID.fromString(username));
    return user;
  }
}
