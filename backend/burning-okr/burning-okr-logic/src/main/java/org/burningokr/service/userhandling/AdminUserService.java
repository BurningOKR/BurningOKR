package org.burningokr.service.userhandling;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.IUser;
import org.burningokr.repositories.users.AdminUserRepository;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminUserService {

  private AdminUserRepository adminUserRepository;

  /**
   * Checks if the Current User is an Admin User.
   *
   * @return a Boolean value
   */
  public Boolean isCurrentUserAdmin() {
    // TODO fix auth (jklein 23.02.2023)
    throw new NotImplementedException("fix auth");
//    UUID currentUserId = userService.getCurrentUser().getId();
//    Optional<AdminUser> currentUserAdminOptional = adminUserRepository.findById(currentUserId);
//
//    return currentUserAdminOptional.isPresent();
  }

  /**
   * Get all Admin Users.
   *
   * @return a {@link Collection} of {@link UUID}
   */
  public Collection<UUID> getAllAdmins() {
    return Lists.newArrayList(adminUserRepository.findAll()).stream()
      .map(AdminUser::getId)
      .collect(Collectors.toList());
  }

  /**
   * Add an Admin User.
   *
   * @param newAdminUser an {@link AdminUser} object
   * @return an {@link IUser} object
   */
  public IUser addAdmin(AdminUser newAdminUser) {
    UUID newAdminUuid = newAdminUser.getId();
    AdminUser newAdmin = new AdminUser();
    newAdmin.setId(newAdminUuid);
    adminUserRepository.save(newAdmin);
    // TODO fix auth (jklein 23.02.2023)
    throw new NotImplementedException("fix auth");
//    return userService.findById(newAdminUser.getId());
  }

  public void removeAdmin(UUID adminToRemove) {
    adminUserRepository.deleteById(adminToRemove);
  }
}
