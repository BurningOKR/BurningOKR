package org.burningokr.service.excel;

import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.service.messages.Messages;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.burningokr.service.userhandling.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
public class TeamMemberRowBuilderService implements RowBuilderService<TeamMemberRow> {

  private final UserService userService;
  private final OkrUnitServiceUsers<OkrDepartment> departmentServiceUsers;
  private final CompanyService companyService;
  private final Messages messages;

  /**
   * Initializes TeamMemberRowBuilderService.
   *
   * @param userService            an {@link UserService} object
   * @param departmentServiceUsers a {@link OkrUnitServiceUsers} object
   * @param companyService         a {@link CompanyService} object
   * @param messages               a {@link Messages} object
   */
  @Autowired
  public TeamMemberRowBuilderService(
    UserService userService,
    @Qualifier("okrUnitServiceUsers") OkrUnitServiceUsers<OkrDepartment> departmentServiceUsers,
    CompanyService companyService,
    Messages messages
  ) {
    this.userService = userService;
    this.departmentServiceUsers = departmentServiceUsers;
    this.companyService = companyService;
    this.messages = messages;
  }

  @Override
  public Collection<TeamMemberRow> generateForOkrChildUnit(long departmentId) {
    OkrDepartment okrDepartment = departmentServiceUsers.findById(departmentId);
    return generateTeamMemberRowForDepartment(okrDepartment);
  }

  private Collection<TeamMemberRow> generateTeamMemberRowForDepartment(
    OkrDepartment okrDepartment
  ) {
    Collection<TeamMemberRow> teamMemberRows = new ArrayList<>();

    if (okrDepartment.getOkrMasterId() != null && !(okrDepartment.getOkrMasterId() == null)) {
      addUserToList(okrDepartment.getOkrMasterId(), okrDepartment, teamMemberRows);
    }
    if (okrDepartment.getOkrTopicSponsorId() != null
      && !(okrDepartment.getOkrTopicSponsorId() == null)) {
      addUserToList(okrDepartment.getOkrTopicSponsorId(), okrDepartment, teamMemberRows);
    }
    okrDepartment
      .getOkrMemberIds()
      .forEach(memberId -> addUserToList(memberId, okrDepartment, teamMemberRows));

    return teamMemberRows;
  }

  private void addUserToList(
    UUID guidUser, OkrDepartment okrDepartment, Collection<TeamMemberRow> rows
  ) {
    User user = userService.findById(guidUser);
    String role = getTeamRoleFromUser(user, okrDepartment);

    TeamMemberRow row =
      new TeamMemberRow(okrDepartment.getName(), role, getFullName(user), user.getMail());
    rows.add(row);
  }

  private String getFullName(User user) {
    return user.getGivenName() + " " + user.getSurname();
  }

  private String getTeamRoleFromUser(User user, OkrDepartment okrDepartment) {
    if (user.getId().equals(okrDepartment.getOkrMasterId())) {
      return messages.get("okrmaster");
    } else if (user.getId().equals(okrDepartment.getOkrTopicSponsorId())) {
      return messages.get("topicsponsor");
    } else {
      for (UUID memberId : okrDepartment.getOkrMemberIds()) {
        if (user.getId().equals(memberId)) {
          return messages.get("teammember");
        }
      }
    }
    return "";
  }

  @Override
  public Collection<TeamMemberRow> generateForCompany(long companyId) {
    OkrCompany okrCompany = companyService.findById(companyId);

    Collection<TeamMemberRow> teamMemberRows = new ArrayList<>();
    Collection<OkrDepartment> okrDepartments = BranchHelper.collectDepartments(okrCompany);
    okrDepartments.forEach(
      department -> teamMemberRows.addAll(generateTeamMemberRowForDepartment(department)));

    return teamMemberRows;
  }
}
