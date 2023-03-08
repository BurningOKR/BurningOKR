package org.burningokr.service.excel;

import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.formula.eval.NotImplementedException;
import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.IUser;
import org.burningokr.service.messages.Messages;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.BranchHelper;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeamMemberRowBuilderService implements RowBuilderService<TeamMemberRow> {
  private final OkrUnitServiceUsers<OkrDepartment> departmentServiceUsers;
  private final CompanyService companyService;
  private final Messages messages;

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
    UUID guidUser, OkrDepartment okrDepartment, Collection<TeamMemberRow> rows
  ) {
    // TODO fix auth (jklein 23.02.2023)
    throw new NotImplementedException("fix auth");
//    User user = null;
//    String role = getTeamRoleFromUser(user, okrDepartment);
//
//    TeamMemberRow row =
//      new TeamMemberRow(okrDepartment.getName(), role, getFullName(user), user.getMail());
//    rows.add(row);
  }

  private String getFullName(IUser IUser) {
    return IUser.getGivenName() + " " + IUser.getSurname();
  }

  private String getTeamRoleFromUser(IUser IUser, OkrDepartment okrDepartment) {
    if (IUser.getId().equals(okrDepartment.getOkrMasterId())) {
      return messages.get("okrmaster");
    } else if (IUser.getId().equals(okrDepartment.getOkrTopicSponsorId())) {
      return messages.get("topicsponsor");
    } else {
      for (UUID memberId : okrDepartment.getOkrMemberIds()) {
        if (IUser.getId().equals(memberId)) {
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
