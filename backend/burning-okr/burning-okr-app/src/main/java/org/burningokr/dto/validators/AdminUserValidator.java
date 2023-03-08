package org.burningokr.dto.validators;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.burningokr.exceptions.InvalidDtoException;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminUserValidator {

  private AdminUserRepository adminUserRepository;

  /**
   * Checks that the given User can be added as an Admin.
   *
   * @param user an {@link AdminUser} object
   * @throws InvalidDtoException if user is null, has an invalid ID, or is already an admin
   */
  public void validateAdminUserOnAdd(AdminUser user) throws InvalidDtoException {
    if (user == null) {
      throw new InvalidDtoException("Input user undefined");
    }
    if (user.getId() == null) {
      throw new InvalidDtoException("ID of user is undefined");
    }

    Optional<AdminUser> optionalAdmin = this.adminUserRepository.findById(user.getId());

    if (optionalAdmin.isPresent()) {
      throw new InvalidDtoException("User is already registered as admin in databank");
    }

    checkIdForSemanticExceptions(user.getId());
  }

  /**
   * Check that user is valid, otherwise throw Exception.
   *
   * @param userId a {@link UUID} object
   * @throws InvalidDtoException if userId is null, is own userId or User is not part of company.
   */
  public void validateAdminUserId(UUID userId) throws InvalidDtoException {
    if (userId == null) {
      throw new InvalidDtoException("Id not set.");
    }

    checkIdForSemanticExceptions(userId);
  }

  private void checkIdForSemanticExceptions(UUID uuid) throws InvalidDtoException {
    // TODO fix auth (jklein 23.02.2023)
    throw new NotImplementedException("fix auth");
  }
}
