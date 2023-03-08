package org.burningokr.service.excel;

import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.service.messages.Messages;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.departmentservices.OkrUnitServiceUsers;
import org.burningokr.service.userhandling.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeamMemberRowBuilderServiceTest {

  private final long companyId = 2L;
  private final long departmentId = 1L;
  private final User IUser1 = mock(User.class);
  private final UUID guidUser1 = UUID.randomUUID();
  private final User IUser2 = mock(User.class);
  private final UUID guidUser2 = UUID.randomUUID();
  private final User IUser3 = mock(User.class);
  private final UUID guidUser3 = UUID.randomUUID();
  @InjectMocks
  TeamMemberRowBuilderService teamMemberRowBuilderService;
  @Mock
  private UserService userService;
  @Mock
  private OkrUnitServiceUsers departmentServiceUsers;
  @Mock
  private CompanyService companyService;
  @Mock
  private Messages messages;
  private OkrCompany okrCompany;
  private OkrDepartment okrDepartment;

  @BeforeEach
  public void init() {
    okrCompany = new OkrCompany();
    okrCompany.setId(companyId);
    okrDepartment = new OkrDepartment();
    okrDepartment.setId(departmentId);
    okrDepartment.setName("My OKR Team");
    when(IUser1.getId()).thenReturn(guidUser1);
    when(IUser1.getMail()).thenReturn("myEmail1@address.com");
    when(IUser2.getId()).thenReturn(guidUser2);
    when(IUser2.getMail()).thenReturn("myEmail2@address.com");
    when(IUser3.getId()).thenReturn(guidUser3);
    when(IUser3.getMail()).thenReturn("myEmail3@address.com");

    when(companyService.findById(companyId)).thenReturn(okrCompany);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(okrDepartment);
    when(userService.findById(guidUser1)).thenReturn(IUser1);
    when(userService.findById(guidUser2)).thenReturn(IUser2);
    when(userService.findById(guidUser3)).thenReturn(IUser3);
    when(messages.get("okrmaster")).thenReturn("OKR Master");
    when(messages.get("topicsponsor")).thenReturn("Topic sponsor");
    when(messages.get("teammember")).thenReturn("Team member");
  }

  @Test
  public void
  generateForDepartment_ShouldReturnEmptyListIfThereAreNoTeamMembersOrOkrMasterOrTopicSponsor() {
    Collection<TeamMemberRow> rows =
      teamMemberRowBuilderService.generateForOkrChildUnit(departmentId);

    assertTrue(rows.isEmpty());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateForDepartment_ShouldReturnListWithOkrMasterAndOkrSponsorAndOkrMembers() {
    okrDepartment.setOkrMasterId(guidUser1);
    okrDepartment.setOkrTopicSponsorId(guidUser2);
    okrDepartment.setOkrMemberIds(Collections.singletonList(guidUser3));

    Collection<TeamMemberRow> rows =
      teamMemberRowBuilderService.generateForOkrChildUnit(departmentId);

    assertEquals(3, rows.size());

    assertEquals(okrDepartment.getName(), ((TeamMemberRow) rows.toArray()[0]).getTeamName());
    assertEquals("OKR Master", ((TeamMemberRow) rows.toArray()[0]).getRole());
    assertEquals(
      IUser1.getGivenName() + " " + IUser1.getSurname(),
      ((TeamMemberRow) rows.toArray()[0]).getFullName()
    );
    assertEquals(IUser1.getMail(), ((TeamMemberRow) rows.toArray()[0]).getEmailAddress());
    assertEquals(okrDepartment.getName(), ((TeamMemberRow) rows.toArray()[1]).getTeamName());
    assertEquals("Topic sponsor", ((TeamMemberRow) rows.toArray()[1]).getRole());
    assertEquals(
      IUser2.getGivenName() + " " + IUser2.getSurname(),
      ((TeamMemberRow) rows.toArray()[1]).getFullName()
    );
    assertEquals(IUser2.getMail(), ((TeamMemberRow) rows.toArray()[1]).getEmailAddress());
    assertEquals(okrDepartment.getName(), ((TeamMemberRow) rows.toArray()[2]).getTeamName());
    assertEquals("Team member", ((TeamMemberRow) rows.toArray()[2]).getRole());
    assertEquals(
      IUser3.getGivenName() + " " + IUser3.getSurname(),
      ((TeamMemberRow) rows.toArray()[2]).getFullName()
    );
    assertEquals(IUser3.getMail(), ((TeamMemberRow) rows.toArray()[2]).getEmailAddress());

    verify(departmentServiceUsers, times(1)).findById(departmentId);
    verify(userService, times(1)).findById(guidUser1);
    verify(userService, times(1)).findById(guidUser2);
    verify(userService, times(1)).findById(guidUser3);
  }

  @Test
  public void
  generateForCompany_ShouldReturnEmptyListIfThereAreNoTeamMembersOrOkrMasterOrTopicSponsor() {
    Collection<TeamMemberRow> rows = teamMemberRowBuilderService.generateForCompany(companyId);

    assertTrue(rows.isEmpty());
    verify(companyService, times(1)).findById(companyId);
  }

  @Test
  public void generateForCompany_ShouldReturnListWithOkrMasterAndOkrSponsorAndOkrMembers() {
    okrCompany.setOkrChildUnits(Collections.singletonList(okrDepartment));
    okrDepartment.setParentOkrUnit(okrCompany);
    okrDepartment.setOkrMasterId(guidUser1);
    okrDepartment.setOkrTopicSponsorId(guidUser2);
    okrDepartment.setOkrMemberIds(Collections.singletonList(guidUser3));

    Collection<TeamMemberRow> rows = teamMemberRowBuilderService.generateForCompany(companyId);

    assertEquals(3, rows.size());

    assertEquals(okrDepartment.getName(), ((TeamMemberRow) rows.toArray()[0]).getTeamName());
    assertEquals("OKR Master", ((TeamMemberRow) rows.toArray()[0]).getRole());
    assertEquals(
      IUser1.getGivenName() + " " + IUser1.getSurname(),
      ((TeamMemberRow) rows.toArray()[0]).getFullName()
    );
    assertEquals(IUser1.getMail(), ((TeamMemberRow) rows.toArray()[0]).getEmailAddress());
    assertEquals(okrDepartment.getName(), ((TeamMemberRow) rows.toArray()[1]).getTeamName());
    assertEquals("Topic sponsor", ((TeamMemberRow) rows.toArray()[1]).getRole());
    assertEquals(
      IUser2.getGivenName() + " " + IUser2.getSurname(),
      ((TeamMemberRow) rows.toArray()[1]).getFullName()
    );
    assertEquals(IUser2.getMail(), ((TeamMemberRow) rows.toArray()[1]).getEmailAddress());
    assertEquals(okrDepartment.getName(), ((TeamMemberRow) rows.toArray()[2]).getTeamName());
    assertEquals("Team member", ((TeamMemberRow) rows.toArray()[2]).getRole());
    assertEquals(
      IUser3.getGivenName() + " " + IUser3.getSurname(),
      ((TeamMemberRow) rows.toArray()[2]).getFullName()
    );
    assertEquals(IUser3.getMail(), ((TeamMemberRow) rows.toArray()[2]).getEmailAddress());

    verify(companyService, times(1)).findById(companyId);
    verify(userService, times(1)).findById(guidUser1);
    verify(userService, times(1)).findById(guidUser2);
    verify(userService, times(1)).findById(guidUser3);
  }
}
