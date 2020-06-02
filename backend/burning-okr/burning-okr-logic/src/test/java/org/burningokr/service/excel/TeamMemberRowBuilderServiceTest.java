package org.burningokr.service.excel;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collection;
import java.util.Collections;
import java.util.UUID;
import org.burningokr.model.excel.TeamMemberRow;
import org.burningokr.model.structures.Company;
import org.burningokr.model.structures.Department;
import org.burningokr.model.users.User;
import org.burningokr.service.messages.Messages;
import org.burningokr.service.structure.CompanyService;
import org.burningokr.service.structure.departmentservices.StructureServiceUsers;
import org.burningokr.service.userhandling.UserService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class TeamMemberRowBuilderServiceTest {

  @Mock private UserService userService;

  @Mock private StructureServiceUsers departmentServiceUsers;

  @Mock private CompanyService companyService;

  @Mock private Messages messages;

  @InjectMocks TeamMemberRowBuilderService teamMemberRowBuilderService;

  private Company company;
  private long companyId = 2L;
  private Department department;
  private long departmentId = 1L;
  private User user1 = mock(User.class);
  private UUID guidUser1 = UUID.randomUUID();
  private User user2 = mock(User.class);
  private UUID guidUser2 = UUID.randomUUID();
  private User user3 = mock(User.class);
  private UUID guidUser3 = UUID.randomUUID();

  @Before
  public void init() {
    company = new Company();
    company.setId(companyId);
    department = new Department();
    department.setId(departmentId);
    department.setName("My OKR Team");
    when(user1.getId()).thenReturn(guidUser1);
    when(user1.getMail()).thenReturn("myEmail1@address.com");
    when(user2.getId()).thenReturn(guidUser2);
    when(user2.getMail()).thenReturn("myEmail2@address.com");
    when(user3.getId()).thenReturn(guidUser3);
    when(user3.getMail()).thenReturn("myEmail3@address.com");

    when(companyService.findById(companyId)).thenReturn(company);
    when(departmentServiceUsers.findById(departmentId)).thenReturn(department);
    when(userService.findById(guidUser1)).thenReturn(user1);
    when(userService.findById(guidUser2)).thenReturn(user2);
    when(userService.findById(guidUser3)).thenReturn(user3);
    when(messages.get("okrmaster")).thenReturn("OKR Master");
    when(messages.get("topicsponsor")).thenReturn("Topic sponsor");
    when(messages.get("teammember")).thenReturn("Team member");
  }

  @Test
  public void
      generateForDepartment_ShouldReturnEmptyListIfThereAreNoTeamMembersOrOkrMasterOrTopicSponsor() {
    Collection<TeamMemberRow> rows =
        teamMemberRowBuilderService.generateForDepartment(departmentId);

    Assert.assertTrue(rows.isEmpty());
    verify(departmentServiceUsers, times(1)).findById(departmentId);
  }

  @Test
  public void generateForDepartment_ShouldReturnListWithOkrMasterAndOkrSponsorAndOkrMembers() {
    department.setOkrMasterId(guidUser1);
    department.setOkrTopicSponsorId(guidUser2);
    department.setOkrMemberIds(Collections.singletonList(guidUser3));

    Collection<TeamMemberRow> rows =
        teamMemberRowBuilderService.generateForDepartment(departmentId);

    Assert.assertEquals(3, rows.size());

    Assert.assertEquals(department.getName(), ((TeamMemberRow) rows.toArray()[0]).getTeamName());
    Assert.assertEquals("OKR Master", ((TeamMemberRow) rows.toArray()[0]).getRole());
    Assert.assertEquals(
        user1.getGivenName() + " " + user1.getSurname(),
        ((TeamMemberRow) rows.toArray()[0]).getFullName());
    Assert.assertEquals(user1.getMail(), ((TeamMemberRow) rows.toArray()[0]).getEmailAddress());
    Assert.assertEquals(department.getName(), ((TeamMemberRow) rows.toArray()[1]).getTeamName());
    Assert.assertEquals("Topic sponsor", ((TeamMemberRow) rows.toArray()[1]).getRole());
    Assert.assertEquals(
        user2.getGivenName() + " " + user2.getSurname(),
        ((TeamMemberRow) rows.toArray()[1]).getFullName());
    Assert.assertEquals(user2.getMail(), ((TeamMemberRow) rows.toArray()[1]).getEmailAddress());
    Assert.assertEquals(department.getName(), ((TeamMemberRow) rows.toArray()[2]).getTeamName());
    Assert.assertEquals("Team member", ((TeamMemberRow) rows.toArray()[2]).getRole());
    Assert.assertEquals(
        user3.getGivenName() + " " + user3.getSurname(),
        ((TeamMemberRow) rows.toArray()[2]).getFullName());
    Assert.assertEquals(user3.getMail(), ((TeamMemberRow) rows.toArray()[2]).getEmailAddress());

    verify(departmentServiceUsers, times(1)).findById(departmentId);
    verify(userService, times(1)).findById(guidUser1);
    verify(userService, times(1)).findById(guidUser2);
    verify(userService, times(1)).findById(guidUser3);
  }

  @Test
  public void
      generateForCompany_ShouldReturnEmptyListIfThereAreNoTeamMembersOrOkrMasterOrTopicSponsor() {
    Collection<TeamMemberRow> rows = teamMemberRowBuilderService.generateForCompany(companyId);

    Assert.assertTrue(rows.isEmpty());
    verify(companyService, times(1)).findById(companyId);
  }

  @Test
  public void generateForCompany_ShouldReturnListWithOkrMasterAndOkrSponsorAndOkrMembers() {
    company.setSubStructures(Collections.singletonList(department));
    department.setParentStructure(company);
    department.setOkrMasterId(guidUser1);
    department.setOkrTopicSponsorId(guidUser2);
    department.setOkrMemberIds(Collections.singletonList(guidUser3));

    Collection<TeamMemberRow> rows = teamMemberRowBuilderService.generateForCompany(companyId);

    Assert.assertEquals(3, rows.size());

    Assert.assertEquals(department.getName(), ((TeamMemberRow) rows.toArray()[0]).getTeamName());
    Assert.assertEquals("OKR Master", ((TeamMemberRow) rows.toArray()[0]).getRole());
    Assert.assertEquals(
        user1.getGivenName() + " " + user1.getSurname(),
        ((TeamMemberRow) rows.toArray()[0]).getFullName());
    Assert.assertEquals(user1.getMail(), ((TeamMemberRow) rows.toArray()[0]).getEmailAddress());
    Assert.assertEquals(department.getName(), ((TeamMemberRow) rows.toArray()[1]).getTeamName());
    Assert.assertEquals("Topic sponsor", ((TeamMemberRow) rows.toArray()[1]).getRole());
    Assert.assertEquals(
        user2.getGivenName() + " " + user2.getSurname(),
        ((TeamMemberRow) rows.toArray()[1]).getFullName());
    Assert.assertEquals(user2.getMail(), ((TeamMemberRow) rows.toArray()[1]).getEmailAddress());
    Assert.assertEquals(department.getName(), ((TeamMemberRow) rows.toArray()[2]).getTeamName());
    Assert.assertEquals("Team member", ((TeamMemberRow) rows.toArray()[2]).getRole());
    Assert.assertEquals(
        user3.getGivenName() + " " + user3.getSurname(),
        ((TeamMemberRow) rows.toArray()[2]).getFullName());
    Assert.assertEquals(user3.getMail(), ((TeamMemberRow) rows.toArray()[2]).getEmailAddress());

    verify(companyService, times(1)).findById(companyId);
    verify(userService, times(1)).findById(guidUser1);
    verify(userService, times(1)).findById(guidUser2);
    verify(userService, times(1)).findById(guidUser3);
  }
}
