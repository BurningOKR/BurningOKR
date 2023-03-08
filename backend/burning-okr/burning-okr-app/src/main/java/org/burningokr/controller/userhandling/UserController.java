package org.burningokr.controller.userhandling;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.users.UserDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;
import java.util.UUID;

@RestApiController
@RequiredArgsConstructor
public class UserController {

  private UserMapper userMapper;

  @GetMapping("/users/current")
  public ResponseEntity<UserDto> getCurrentUser() {
    // TODO fix auth (jklein 23.02.2023)
    throw new NotImplementedException("fix auth");
  }

  @GetMapping("/users")
  public ResponseEntity<Collection<UserDto>> getAllUsers(
    @RequestParam(value = "activeUsers", required = false) Boolean activeUsers
  ) {
    // TODO fix auth (jklein 23.02.2023)
    throw new NotImplementedException("fix auth");
  }

  @GetMapping("/users/{userId}")
  public ResponseEntity<UserDto> getUserByInformation(
    @PathVariable UUID userId
  ) {
    // TODO fix auth (jklein 23.02.2023)
    throw new NotImplementedException("fix auth");
  }
}
