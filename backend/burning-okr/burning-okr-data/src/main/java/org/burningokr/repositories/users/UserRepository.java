package org.burningokr.repositories.users;

import org.burningokr.model.users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
  List<User> findAllByActiveIsTrue();
  List<User> findAllByActiveIsFalse();
}
