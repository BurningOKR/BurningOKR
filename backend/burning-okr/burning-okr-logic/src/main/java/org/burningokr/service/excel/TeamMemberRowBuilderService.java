package org.burningokr.service.excel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;
import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.service.messages.Messages;
import org.burningokr.service.structure.CompanyService;
import org.burningokr.service.structure.departmentservices.StructureHelper;
import org.burningokr.service.structure.departmentservices.StructureServiceUsers;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class TeamMemberRowBuilderService implements RowBuilderService<TeamMemberRow> {

  private final UserService userService;
  private final StructureServiceUsers<Department> departmentServiceUsers;
  private final CompanyService companyService;
  private final Messages messages;

  /**
   * Initializes TeamMemberRowBuilderService.
   *
   * @param userService an {@link UserService} object
   * @param departmentServiceUsers a {@link StructureServiceUsers} object
   * @param companyService a {@link CompanyService} object
   * @param messages a {@link Messages} object
   */
  @Autowired
  public TeamMemberRowBuilderService(
      UserService userService,
      @Qualifier("structureServiceUsers") StructureServiceUsers<Department> departmentServiceUsers,
      CompanyService companyService,
      Messages messages) {
    this.userService = userService;
    this.departmentServiceUsers = departmentServiceUsers;
    this.companyService = companyService;
    this.messages = messages;
  }

  @Override
  public Collection<TeamMemberRow> generateForDepartment(long departmentId) {
    Department department = departmentServiceUsers.findById(departmentId);
    return generateTeamMemberRowForDepartment(department);
  }

  private Collection<TeamMemberRow> generateTeamMemberRowForDepartment(Department department) {
    Collection<TeamMemberRow> teamMemberRows = new ArrayList<>();

    if (department.getOkrMasterId() != null && !(department.getOkrMasterId() == null)) {
      addUserToList(department.getOkrMasterId(), department, teamMemberRows);
    }
    if (department.getOkrTopicSponsorId() != null && !(department.getOkrTopicSponsorId() == null)) {
      addUserToList(department.getOkrTopicSponsorId(), department, teamMemberRows);
    }
    department
        .getOkrMemberIds()
        .forEach(memberId -> addUserToList(memberId, department, teamMemberRows));

    return teamMemberRows;
  }

  private void addUserToList(UUID guidUser, Department department, Collection<TeamMemberRow> rows) {
    User user = userService.findById(guidUser);
    String role = getTeamRoleFromUser(user, department);

    TeamMemberRow row =
        new TeamMemberRow(department.getName(), role, getFullName(user), user.getMail());
    rows.add(row);
  }

  private String getFullName(User user) {
    return user.getGivenName() + " " + user.getSurname();
  }

  private String getTeamRoleFromUser(User user, Department department) {
    if (user.getId().equals(department.getOkrMasterId())) {
      return messages.get("okrmaster");
    } else if (user.getId().equals(department.getOkrTopicSponsorId())) {
      return messages.get("topicsponsor");
    } else {
      for (UUID memberId : department.getOkrMemberIds()) {
        if (user.getId().equals(memberId)) {
          return messages.get("teammember");
        }
      }
    }
    return "";
  }

  @Override
  public Collection<TeamMemberRow> generateForCompany(long companyId) {
    Company company = companyService.findById(companyId);

    Collection<TeamMemberRow> teamMemberRows = new ArrayList<>();
    Collection<Department> departments = StructureHelper.collectDepartments(company);
    departments.forEach(
        department -> teamMemberRows.addAll(generateTeamMemberRowForDepartment(department)));

    return teamMemberRows;
  }
}
