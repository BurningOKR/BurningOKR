package org.burningokr.service;

import java.util.Optional;
import javax.persistence.EntityNotFoundException;
import org.burningokr.config.authorizationserver.LocalUserDetails;
import org.burningokr.model.users.LocalUser;
import org.burningokr.repositories.users.LocalUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class LocalUserDetailsService implements UserDetailsService {
  private final LocalUserRepository localUserRepository;

  @Autowired
  public LocalUserDetailsService(LocalUserRepository localUserRepository) {

    this.localUserRepository = localUserRepository;
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Optional<LocalUser> opt = localUserRepository.findByMail(username.toLowerCase());
    if (opt.isPresent()) {
      return new LocalUserDetails(opt.get());
    }
    throw new EntityNotFoundException("Entity not found.");
  }
}
