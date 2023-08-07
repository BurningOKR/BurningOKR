package org.burningokr.controller.userhandling;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.burningokr.annotation.RestApiController;
import org.burningokr.dto.users.UserDto;
import org.burningokr.mapper.users.UserMapper;
import org.burningokr.model.users.User;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.burningokr.service.userhandling.UserService;
import org.hibernate.validator.constraints.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@RestApiController()
@RequiredArgsConstructor
@Validated
@Slf4j
public class UserController {

  private final UserMapper userMapper;
  private final UserService userService;
  private final AuthenticationUserContextService authenticationUserContextService;

  @GetMapping("/users/current")
  public ResponseEntity<UserDto> getCurrentUser() {
    var currentUser = authenticationUserContextService.getAuthenticatedUser();
    return ResponseEntity.ok(userMapper.mapEntityToDto(currentUser));
  }

  @GetMapping("/users")
  public ResponseEntity<Collection<UserDto>> getAllUsers(
          @RequestParam(value = "activeUsers", required = false) Boolean activeUsers
  ) {
    Collection<User> userCollection;

    if (activeUsers == null) {
      userCollection = userService.findAll();
    } else if (activeUsers) {
      userCollection = userService.findAllActive();
    } else {
      userCollection = userService.findAllInactive();
    }

    return ResponseEntity.ok(userMapper.mapEntitiesToDtos(userCollection));
  }

  @GetMapping("/admins")
  @PreAuthorize("@authorizationService.isAdmin()")
  public ResponseEntity<Collection<UserDto>> getAllAdmins() {
    Collection<User> adminUserCollection;

    adminUserCollection = userService.findAll();

    return ResponseEntity.ok(userMapper.mapEntitiesToDtos(
            adminUserCollection.stream()
                    .filter(User::isAdmin)
                    .toList()));
  }


  @GetMapping("/users/{userId}")
  public ResponseEntity<UserDto> getUserById(
      @PathVariable @UUID String userId
  ) {
    var user = userService.findById(java.util.UUID.fromString(userId));
    return ResponseEntity.ok(userMapper.mapEntityToDto(user.orElseThrow(EntityNotFoundException::new)));
  }

  @ExceptionHandler(ConstraintViolationException.class)
  public ResponseEntity<String> handleConstraintViolation(Exception ex) {
    log.warn(("received invalid path variable or request param, error message: %s, returning error message and" +
        " resuming with normal operation").formatted(ex.getMessage()));
    return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
  }
}
