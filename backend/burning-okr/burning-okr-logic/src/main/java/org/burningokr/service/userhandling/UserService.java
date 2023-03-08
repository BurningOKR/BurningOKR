package org.burningokr.service.userhandling;

import org.burningokr.model.users.IUser;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;

@Service
public interface UserService {
  Collection<IUser> findAll();

  Collection<IUser> findAllActive();

  Collection<IUser> findAllInactive();

  IUser getCurrentUser();

  IUser findById(UUID userId);

  boolean doesUserExist(UUID userId);
}
