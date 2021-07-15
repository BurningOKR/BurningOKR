package org.burningokr.mapper.okr;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OkrTopicDraftMapperTest {

  @Mock private UserService userService;

  @Mock private User user;

  private OkrTopicDraft topicDraft;
  private OkrTopicDraftDto topicDraftDto;
  private OkrTopicDraftMapper mapper;

  @Before
  public void setUp() {
    topicDraft = new OkrTopicDraft();
    topicDraftDto = new OkrTopicDraftDto();
    mapper = new OkrTopicDraftMapper(userService);
    when(userService.findById(any())).thenReturn(user);
  }

  // Entity To Dto

  @Test
  public void test_mapEntityToDto_expect_id_ismapped() {
    topicDraft.setId(10l);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(10L, actual.getId().longValue());
  }

  @Test
  public void test_mapEntityToDto_expect_name_ismapped() {
    String expected = "test";

    topicDraft.setName(expected);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(expected, actual.getName());
  }

  @Test
  public void test_mapEntityToDto_expect_initiatorId_ismapped() {
    UUID initiatorId = UUID.randomUUID();

    topicDraft.setInitiatorId(initiatorId);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(initiatorId, actual.getInitiatorId());
  }

  @Test
  public void test_mapEntityToDto_expect_acceptanceCriteria_ismapped() {
    String expected = "testAcceptanceCriteria";

    topicDraft.setDescription(expected);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void test_mapEntityToDto_expect_contributesTo_ismapped() {
    String expected = "testContributesTo";

    topicDraft.setContributesTo(expected);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void test_mapEntityToDto_expect_delimitation_ismapped() {
    String expected = "testDelimitation";

    topicDraft.setDelimitation(expected);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(expected, actual.getDelimitation());
  }

  @Test
  public void test_mapEntityToDto_expect_beginning_ismapped() {
    LocalDate beginning = LocalDate.of(2020, 3, 1);

    topicDraft.setBeginning(beginning);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(beginning, actual.getBeginning());
  }

  @Test
  public void test_mapEntityToDto_expect_dependencies_ismapped() {
    String expected = "testDependencies";

    topicDraft.setDependencies(expected);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void test_mapEntityToDto_expect_resources_ismapped() {
    String expected = "testResources";

    topicDraft.setResources(expected);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(expected, actual.getResources());
  }

  @Test
  public void test_mapEntityToDto_expect_handoverPlan_ismapped() {
    String expected = "testHandoverPlan";

    topicDraft.setHandoverPlan(expected);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(expected, actual.getHandoverPlan());
  }

  @Test
  public void test_mapEntityToDto_expect_startTeam_ismapped() {
    UUID member1 = UUID.randomUUID();
    UUID member2 = UUID.randomUUID();
    UUID member3 = UUID.randomUUID();

    List<UUID> members = new ArrayList<UUID>();
    members.add(member1);
    members.add(member2);
    members.add(member3);

    topicDraft.setStartTeam(members);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertTrue(actual.getStartTeam().contains(member1));
    assertTrue(actual.getStartTeam().contains(member2));
    assertTrue(actual.getStartTeam().contains(member3));
  }

  @Test
  public void test_mapEntityToDto_expect_stakeholders_ismapped() {
    UUID member1 = UUID.randomUUID();
    UUID member2 = UUID.randomUUID();
    UUID member3 = UUID.randomUUID();

    List<UUID> members = new ArrayList<UUID>();
    members.add(member1);
    members.add(member2);
    members.add(member3);

    topicDraft.setStakeholders(members);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertTrue(actual.getStakeholders().contains(member1));
    assertTrue(actual.getStakeholders().contains(member2));
    assertTrue(actual.getStakeholders().contains(member3));
  }

  @Test
  public void test_mapEntityToDto_expect_parentUnit_ismapped() {
    OkrUnit expected = new OkrBranch();
    expected.setId(15l);

    topicDraft.setParentUnit(expected);
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);
    assertEquals(expected.getId(), actual.getOkrParentUnitId());
  }

  @Test
  public void test_mapEntityToDto_expect_initiator_isSet() {
    OkrTopicDraftDto actual = mapper.mapEntityToDto(topicDraft);

    assertSame(user, actual.getInitiator());
  }

  @Test
  public void test_mapEntityToDto_expect_findById_isCalled() {
    mapper.mapEntityToDto(topicDraft);

    verify(userService).findById(any());
  }

  @Test
  public void test_mapEntityToDto_expect_currentStatus_ismapped() {
    topicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);

    OkrTopicDraftDto mappedDto = mapper.mapEntityToDto(topicDraft);
    assertEquals(topicDraft.getCurrentStatus().ordinal(), mappedDto.getCurrentStatus());
  }

  // Dto to Entity

  @Test
  public void test_mapDtoToEntity_expect_id_ismapped() {
    topicDraftDto.setId(10l);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(10L, actual.getId().longValue());
  }

  @Test
  public void test_mapDtoToEntity_expect_name_ismapped() {
    String expected = "test";

    topicDraftDto.setName(expected);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(expected, actual.getName());
  }

  @Test
  public void test_mapDtoToEntity_expect_initiatorId_ismapped() {
    UUID initiatorId = UUID.randomUUID();

    topicDraftDto.setInitiatorId(initiatorId);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(initiatorId, actual.getInitiatorId());
  }

  @Test
  public void test_mapDtoToEntity_expect_acceptanceCriteria_ismapped() {
    String expected = "testAcceptanceCriteria";

    topicDraftDto.setDescription(expected);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void test_mapDtoToEntity_expect_contributesTo_ismapped() {
    String expected = "testContributesTo";

    topicDraftDto.setContributesTo(expected);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void test_mapDtoToEntity_expect_delimitation_ismapped() {
    String expected = "testDelimitation";

    topicDraftDto.setDelimitation(expected);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(expected, actual.getDelimitation());
  }

  @Test
  public void test_mapDtoToEntity_expect_beginning_ismapped() {
    LocalDate beginning = LocalDate.of(2020, 3, 1);

    topicDraftDto.setBeginning(beginning);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(beginning, actual.getBeginning());
  }

  @Test
  public void test_mapDtoToEntity_expect_dependencies_ismapped() {
    String expected = "testDependencies";

    topicDraftDto.setDependencies(expected);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void test_mapDtoToEntity_expect_resources_ismapped() {
    String expected = "testResources";

    topicDraftDto.setResources(expected);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(expected, actual.getResources());
  }

  @Test
  public void test_mapDtoToEntity_expect_handoverPlan_ismapped() {
    String expected = "testHandoverPlan";

    topicDraftDto.setHandoverPlan(expected);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(expected, actual.getHandoverPlan());
  }

  @Test
  public void test_mapDtoToEntity_expect_startTeam_ismapped() {
    UUID member1 = UUID.randomUUID();
    UUID member2 = UUID.randomUUID();
    UUID member3 = UUID.randomUUID();

    List<UUID> members = new ArrayList<UUID>();
    members.add(member1);
    members.add(member2);
    members.add(member3);

    topicDraftDto.setStartTeam(members);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertTrue(actual.getStartTeam().contains(member1));
    assertTrue(actual.getStartTeam().contains(member2));
    assertTrue(actual.getStartTeam().contains(member3));
  }

  @Test
  public void test_mapDtoToEntity_expect_stakeholders_ismapped() {
    UUID member1 = UUID.randomUUID();
    UUID member2 = UUID.randomUUID();
    UUID member3 = UUID.randomUUID();

    List<UUID> members = new ArrayList<UUID>();
    members.add(member1);
    members.add(member2);
    members.add(member3);

    topicDraftDto.setStakeholders(members);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertTrue(actual.getStakeholders().contains(member1));
    assertTrue(actual.getStakeholders().contains(member2));
    assertTrue(actual.getStakeholders().contains(member3));
  }

  @Test
  public void test_mapDtoToEntity_expect_parentUnit_isnotmapped() {
    long id = 15l;

    topicDraftDto.setOkrParentUnitId(id);
    OkrTopicDraft actual = mapper.mapDtoToEntity(topicDraftDto);
    assertNull(actual.getParentUnit());
  }

  @Test
  public void test_mapDtoToEntity_expect_currentStatus_ismapped() {
    topicDraftDto.setCurrentStatus(OkrTopicDraftStatusEnum.submitted.ordinal());

    OkrTopicDraft mappedTopicDraft = mapper.mapDtoToEntity(topicDraftDto);
    assertEquals(topicDraftDto.getCurrentStatus(), mappedTopicDraft.getCurrentStatus().ordinal());
  }
}
