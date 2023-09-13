package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraftStatusEnum;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrUnit;
import org.burningokr.model.users.User;
import org.burningokr.service.userhandling.UserService;
import org.burningokr.service.util.DateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OkrTopicDraftMapperTest {

  @Mock
  private UserService userService;

  @Mock
  private User user;

  private OkrTopicDraft topicDraft;
  private OkrTopicDraftDto topicDraftDto;
  private OkrTopicDraftMapper topicDraftMapper;

  @BeforeEach
  public void setUp() {
    DateMapper dateMapper = new DateMapper();
    topicDraft = new OkrTopicDraft();
    topicDraftDto = new OkrTopicDraftDto();
    topicDraftMapper = new OkrTopicDraftMapper(userService, dateMapper);

  }

  @Test
  public void mapEntityToDto_shouldMapId() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    long expected = 10L;
    topicDraft.setId(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getId().longValue());
  }

  @Test
  public void mapEntityToDto_shouldMapName() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    String expected = "test";
    topicDraft.setName(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getName());
  }

  @Test
  public void mapEntityToDto_shouldMapInitiatorId() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    UUID expected = UUID.randomUUID();
    topicDraft.setInitiatorId(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getInitiatorId());
  }

  @Test
  public void mapEntityToDto_shouldMapDescription() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    String expected = "testAcceptanceCriteria";
    topicDraft.setDescription(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void mapEntityToDto_shouldMapContributesTo() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    String expected = "testContributesTo";
    topicDraft.setContributesTo(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void test_mapEntityToDto_shouldMapDelimitation() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    String expected = "testDelimitation";
    topicDraft.setDelimitation(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getDelimitation());
  }

  @Test
  public void mapEntityToDto_shouldMapBeginningDate() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    LocalDate expected = LocalDate.of(2020, 3, 1);
    topicDraft.setBeginning(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected.toString(), actual.getBeginning());
  }

  @Test
  public void mapEntityToDto_shouldMapDependencies() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    String expected = "testDependencies";
    topicDraft.setDependencies(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void mapEntityToDto_shouldMapResources() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    String expected = "testResources";
    topicDraft.setResources(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getResources());
  }

  @Test
  public void mapEntityToDto_shouldMapHandoverPlan() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    String expected = "testHandoverPlan";
    topicDraft.setHandoverPlan(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getHandoverPlan());
  }

  @Test
  public void mapEntityToDto_shouldMapStartTeam() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    UUID member1 = UUID.randomUUID();
    UUID member2 = UUID.randomUUID();
    UUID member3 = UUID.randomUUID();
    List<UUID> members = new ArrayList<>() {
      {
        add(member1);
        add(member2);
        add(member3);
      }
    };
    topicDraft.setStartTeam(members);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertTrue(actual.getStartTeam().contains(member1));
    assertTrue(actual.getStartTeam().contains(member2));
    assertTrue(actual.getStartTeam().contains(member3));
  }

  @Test
  public void mapEntityToDto_shouldMapStakeholders() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    UUID member1 = UUID.randomUUID();
    UUID member2 = UUID.randomUUID();
    UUID member3 = UUID.randomUUID();
    List<UUID> members = new ArrayList<>() {
      {
        add(member1);
        add(member2);
        add(member3);
      }
    };
    topicDraft.setStakeholders(members);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertTrue(actual.getStakeholders().contains(member1));
    assertTrue(actual.getStakeholders().contains(member2));
    assertTrue(actual.getStakeholders().contains(member3));
  }

  @Test
  public void mapEntityToDto_shouldMapParentUnit() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    OkrUnit expected = new OkrBranch();
    expected.setId(15L);
    topicDraft.setParentUnit(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected.getId(), actual.getOkrParentUnitId());
  }

  @Test
  public void mapEntityToDto_shouldCheckIfIsInitiatorIsSet() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertSame(user, actual.getInitiator());
  }

  @Test
  public void mapEntityToDto_shouldVerifyFindById() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    topicDraftMapper.mapEntityToDto(topicDraft);

    verify(userService).findById(any());
  }

  @Test
  public void mapEntityToDto_shouldMapCurrentStatus() {
    when(userService.findById(any())).thenReturn(Optional.of(user));
    topicDraft.setCurrentStatus(OkrTopicDraftStatusEnum.submitted);

    OkrTopicDraftDto mappedDto = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(topicDraft.getCurrentStatus().ordinal(), mappedDto.getCurrentStatus());
  }

  @Test
  public void mapDtoToEntity_shouldMapId() {
    long expected = 10L;
    topicDraftDto.setId(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getId().longValue());
  }

  @Test
  public void mapDtoToEntity_shouldMapName() {
    String expected = "test";
    topicDraftDto.setName(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getName());
  }

  @Test
  public void mapDtoToEntity_shouldMapInitiatorId() {
    UUID expected = UUID.randomUUID();
    topicDraftDto.setInitiatorId(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getInitiatorId());
  }

  @Test
  public void mapDtoToEntity_shouldMapAcceptanceCriteria() {
    String expected = "testAcceptanceCriteria";
    topicDraftDto.setDescription(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void mapDtoToEntity_shouldMapContributesTo() {
    String expected = "testContributesTo";
    topicDraftDto.setContributesTo(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void mapDtoToEntity_shouldMapDelimitation() {
    String expected = "testDelimitation";
    topicDraftDto.setDelimitation(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getDelimitation());
  }


  @Test
  public void mapDtoToEntity_shouldMapBeginning() {
    LocalDate expected = LocalDate.of(2020, 3, 1);
    topicDraftDto.setBeginning(expected.toString());

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getBeginning());
  }

  @Test
  public void mapDtoToEntity_shouldMapDependencies() {
    String expected = "testDependencies";
    topicDraftDto.setDependencies(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void mapDtoToEntity_shouldMapResources() {
    String expected = "testResources";
    topicDraftDto.setResources(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getResources());
  }

  @Test
  public void mapDtoToEntity_shouldMapHandoverPlan() {
    String expected = "testHandoverPlan";
    topicDraftDto.setHandoverPlan(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(expected, actual.getHandoverPlan());
  }

  @Test
  public void mapDtoToEntity_shouldMapStartTeam() {
    UUID member1 = UUID.randomUUID();
    UUID member2 = UUID.randomUUID();
    UUID member3 = UUID.randomUUID();
    List<UUID> members = new ArrayList<>() {
      {
        add(member1);
        add(member2);
        add(member3);
      }
    };
    topicDraftDto.setStartTeam(members);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertTrue(actual.getStartTeam().contains(member1));
    assertTrue(actual.getStartTeam().contains(member2));
    assertTrue(actual.getStartTeam().contains(member3));
  }

  @Test
  public void mapDtoToEntity_shouldMapStakeholders() {
    UUID member1 = UUID.randomUUID();
    UUID member2 = UUID.randomUUID();
    UUID member3 = UUID.randomUUID();
    List<UUID> members = new ArrayList<>() {
      {
        add(member1);
        add(member2);
        add(member3);
      }
    };
    topicDraftDto.setStakeholders(members);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertTrue(actual.getStakeholders().contains(member1));
    assertTrue(actual.getStakeholders().contains(member2));
    assertTrue(actual.getStakeholders().contains(member3));
  }

  @Test
  public void mapDtoToEntity_shouldNotMapParentUnit() {
    long expected = 15L;
    topicDraftDto.setOkrParentUnitId(expected);

    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertNull(actual.getParentUnit());
  }

  @Test
  public void mapDtoToEntity_shouldMapCurrentStatus() {
    topicDraftDto.setCurrentStatus(OkrTopicDraftStatusEnum.submitted.ordinal());

    OkrTopicDraft mappedTopicDraft = topicDraftMapper.mapDtoToEntity(topicDraftDto);

    assertEquals(topicDraftDto.getCurrentStatus(), mappedTopicDraft.getCurrentStatus().ordinal());
  }

  @Test
  public void mapDtosToEntities_shouldMapNoteDtosToEntities() {
    topicDraftDto.setId(12L);
    Collection<OkrTopicDraftDto> expected = new ArrayList<>() {
      {
        add(topicDraftDto);
        add(topicDraftDto);
      }
    };
    Collection<OkrTopicDraft> actual = topicDraftMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getId());
  }

  @Test
  public void mapDtosToEntities_shouldHandleEmptyList() {
    Collection<OkrTopicDraftDto> expected = new ArrayList<>() {};
    Collection<OkrTopicDraft> actual = topicDraftMapper.mapDtosToEntities(expected);
    assertEquals(expected.size(), actual.size());
  }

  @Test
  public void mapEntitiesToDtos_shouldMapNoteEntitiesToDtos() {
    when(userService.findById(any())).thenReturn(Optional.of(user));

    topicDraft.setId(12L);
    Collection<OkrTopicDraft> expected = new ArrayList<>() {
      {
        add(topicDraft);
        add(topicDraft);
      }
    };
    Collection<OkrTopicDraftDto> actual = topicDraftMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
    assertEquals(expected.stream().findFirst().orElseThrow().getId(), actual.stream().findFirst().orElseThrow().getId());
  }

  @Test
  public void mapEntitiesToDtos_shouldHandleEmptyList() {
    Collection<OkrTopicDraft> expected = new ArrayList<>() {};
    Collection<OkrTopicDraftDto> actual = topicDraftMapper.mapEntitiesToDtos(expected);
    assertEquals(expected.size(), actual.size());
  }
}
