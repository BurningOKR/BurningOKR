package org.burningokr.controller.userhandling;

import java.util.Collection;
import java.util.UUID;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.burningokr.annotation.RestApiController;
import org.burningokr.config.condition.LocalUserCondition;
import org.burningokr.dto.users.ChangePasswordDto;
import org.burningokr.dto.users.ForgotPasswordDto;
import org.burningokr.dto.users.LocalUserDto;
import org.burningokr.dto.users.PasswordDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.users.ChangePasswordMapper;
import org.burningokr.mapper.users.LocalUserMapper;
import org.burningokr.model.users.ChangePasswordData;
import org.burningokr.model.users.ForgotPassword;
import org.burningokr.model.users.LocalUser;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.LocalUserService;
import org.springframework.context.annotation.Conditional;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestApiController
@RequiredArgsConstructor
@Conditional(LocalUserCondition.class)
public class LocalUserController {

  private final LocalUserService localUserService;
  private final LocalUserMapper localUserMapper;
  private final ChangePasswordMapper changePasswordMapper;
  private final DataMapper<ForgotPassword, ForgotPasswordDto> forgotPasswordMapper;

  @GetMapping("/local-users")
  public ResponseEntity<Collection<LocalUserDto>> getAllUsers() {
    return ResponseEntity.ok(
        localUserMapper.mapEntitiesToDtos(localUserService.retrieveAllUsers()));
  }

  @GetMapping("/local-users/{userId}")
  public ResponseEntity<LocalUserDto> getUserById(@PathVariable UUID userId) {
    LocalUser localUser = localUserService.retrieveLocalUserById(userId);
    return ResponseEntity.ok(localUserMapper.mapEntityToDto(localUser));
  }

  /**
   * API Endpoint to create a Local User.
   *
   * @param localUserDto a {@link LocalUserDto} object
   * @return a {@link ResponseEntity} with status ok and a {@link LocalUserDto} object
   */
  @PostMapping("/local-users")
  public ResponseEntity<LocalUserDto> createLocalUser(
      @Valid @RequestBody LocalUserDto localUserDto) {
    LocalUser localUser =
        localUserService.createLocalUser(localUserMapper.mapDtoToEntity(localUserDto));
    return ResponseEntity.ok(localUserMapper.mapEntityToDto(localUser));
  }

  /**
   * API Endpoint to create a list of Local Users.
   *
   * @param localUserDtos a {@link Collection} of {@link LocalUserDto} objects
   * @return a {@link ResponseEntity} with status ok and a {@link Collection} of {@link
   *     LocalUserDto}
   */
  @PostMapping("/local-users/bulk")
  public ResponseEntity<Collection<LocalUserDto>> bulkCreateLocalUsers(
      @Valid @RequestBody Collection<LocalUserDto> localUserDtos) {
    Collection<User> localUsers =
        localUserService.bulkCreateLocalUsers(localUserMapper.mapDtosToEntities(localUserDtos));
    return ResponseEntity.ok(localUserMapper.mapEntitiesToDtos(localUsers));
  }

  /**
   * API Endpoint to update the Local User.
   *
   * @param userId an {@link UUID}
   * @param localUserDto a {@link LocalUserDto} object
   * @return a {@link ResponseEntity} with status ok and the updated {@link LocalUserDto}
   */
  @PutMapping("/local-users/{userId}")
  public ResponseEntity<LocalUserDto> updateLocalUser(
      @PathVariable UUID userId, @Valid @RequestBody LocalUserDto localUserDto) {
    LocalUser responseUser =
        localUserService.updateLocalUser(userId, localUserMapper.mapDtoToEntity(localUserDto));
    return ResponseEntity.ok(localUserMapper.mapEntityToDto(responseUser));
  }

  @DeleteMapping("/local-users/{userId}")
  public ResponseEntity<LocalUserDto> deleteLocalUserById(@PathVariable UUID userId) {
    localUserService.deleteLocalUserById(userId);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/local-users/password")
  public ResponseEntity<PasswordDto> setPassword(@Valid @RequestBody PasswordDto passwordDto) {
    localUserService.setPassword(passwordDto.getEmailIdentifier(), passwordDto.getPassword());
    return ResponseEntity.ok().build();
  }

  /**
   * API Endpoint to change the Password.
   *
   * @param changePasswordDto a {@link ChangePasswordDto} object
   * @return a {@link ResponseEntity} with status ok
   */
  @PostMapping("/local-users/change-password")
  public ResponseEntity<ChangePasswordDto> changePassword(
      @Valid @RequestBody ChangePasswordDto changePasswordDto) {
    ChangePasswordData changePasswordData =
        this.changePasswordMapper.mapDtoToEntity(changePasswordDto);

    localUserService.changePassword(changePasswordData);
    return ResponseEntity.ok().build();
  }

  /**
   * API Endpoint when a user forgot his/her password.
   *
   * @param forgotPasswordDto a {@link ForgotPasswordDto} object
   * @return a {@link ResponseEntity} with status ok
   */
  @PostMapping("/local-users/forgot-password")
  public ResponseEntity<ForgotPasswordDto> resetPassword(
      @Valid @RequestBody ForgotPasswordDto forgotPasswordDto) {
    ForgotPassword forgotPassword = forgotPasswordMapper.mapDtoToEntity(forgotPasswordDto);
    localUserService.resetPassword(forgotPassword);
    return ResponseEntity.ok().build();
  }
}
