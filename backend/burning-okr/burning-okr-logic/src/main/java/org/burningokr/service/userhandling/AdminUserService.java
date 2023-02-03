package org.burningokr.service.userhandling;

import com.google.common.collect.Lists;
import org.burningokr.model.users.AdminUser;
import org.burningokr.model.users.User;
import org.burningokr.repositories.users.AdminUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AdminUserService {

  private AdminUserRepository adminUserRepository;
  private UserService userService;

  @Autowired
  public AdminUserService(AdminUserRepository adminUserRepository, UserService userService) {
    this.adminUserRepository = adminUserRepository;
    this.userService = userService;
  }

  /**
   * Checks if the Current User is an Admin User.
   *
   * @return a Boolean value
   */
  public Boolean isCurrentUserAdmin() {
    UUID currentUserId = userService.getCurrentUser().getId();
    Optional<AdminUser> currentUserAdminOptional = adminUserRepository.findById(currentUserId);

    return currentUserAdminOptional.isPresent();
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
   * @return an {@link User} object
   */
  public User addAdmin(AdminUser newAdminUser) {
    UUID newAdminUuid = newAdminUser.getId();
    AdminUser newAdmin = new AdminUser();
    newAdmin.setId(newAdminUuid);
    adminUserRepository.save(newAdmin);

    return userService.findById(newAdminUser.getId());
  }

  public void removeAdmin(UUID adminToRemove) {
    adminUserRepository.deleteById(adminToRemove);
  }
}
