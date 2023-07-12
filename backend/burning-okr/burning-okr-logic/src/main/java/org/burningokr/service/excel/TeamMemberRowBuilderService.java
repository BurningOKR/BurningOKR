package org.burningokr.service.excel;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.service.messages.Messages;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.burningokr.service.userhandling.UserService;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamMemberRowBuilderService implements RowBuilderService<TeamMemberRow> {
  private final OkrChildUnitService<OkrDepartment> departmentServiceUsers;
  private final CompanyService companyService;
  private final Messages messages;
  private final UserService userService;

  @Override
  public Collection<TeamMemberRow> generateForOkrChildUnit(long departmentId) {
    OkrDepartment okrDepartment = departmentServiceUsers.findById(departmentId);
    return generateTeamMemberRowForDepartment(okrDepartment);
  }

  private Collection<TeamMemberRow> generateTeamMemberRowForDepartment(
    OkrDepartment okrDepartment
  ) {
    Collection<TeamMemberRow> teamMemberRows = new ArrayList<>();

    if (okrDepartment.getOkrMasterId() != null) {
      addUserToList(okrDepartment.getOkrMasterId(), okrDepartment, teamMemberRows);
    }
    if (okrDepartment.getOkrTopicSponsorId() != null) {
      addUserToList(okrDepartment.getOkrTopicSponsorId(), okrDepartment, teamMemberRows);
    }
    okrDepartment
      .getOkrMemberIds()
      .forEach(memberId -> addUserToList(memberId, okrDepartment, teamMemberRows));

    return teamMemberRows;
  }

  private void addUserToList(
    UUID userId, OkrDepartment okrDepartment, Collection<TeamMemberRow> rows
  ) {
    var user = userService.findById(userId).orElseThrow(EntityNotFoundException::new);

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
