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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class OkrTopicDraftMapperTest {

  @Mock
  private UserService userService;

  @Mock
  private DateMapper dateMapper;

  @Mock
  private User user;

  private OkrTopicDraft topicDraft;
  private OkrTopicDraftDto topicDraftDto;
  private OkrTopicDraftMapper topicDraftMapper;

  @BeforeEach
  public void setUp() {
    topicDraft = new OkrTopicDraft();
    topicDraftDto = new OkrTopicDraftDto();
    topicDraftMapper = new OkrTopicDraftMapper(userService, dateMapper);
    Mockito.lenient().when(userService.findById(any())).thenReturn(Optional.of(user));
  }

  @Test
  public void mapEntityToDto_shouldMapId() {
    long expected = 10L;
    topicDraft.setId(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getId().longValue());
  }

  @Test
  public void mapEntityToDto_shouldMapName() {
    String expected = "test";
    topicDraft.setName(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getName());
  }

  @Test
  public void mapEntityToDto_shouldMapInitiatorId() {
    UUID expected = UUID.randomUUID();
    topicDraft.setInitiatorId(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getInitiatorId());
  }

  @Test
  public void mapEntityToDto_shouldMapDescription() {
    String expected = "testAcceptanceCriteria";
    topicDraft.setDescription(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void mapEntityToDto_shouldMapContributesTo() {
    String expected = "testContributesTo";
    topicDraft.setContributesTo(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void test_mapEntityToDto_shouldMapDelimitation() {
    String expected = "testDelimitation";
    topicDraft.setDelimitation(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getDelimitation());
  }

  @Test
  public void mapEntityToDto_shouldMapBeginningDate() {
    LocalDate expected = LocalDate.of(2020, 3, 1);
    topicDraft.setBeginning(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected.toString(), actual.getBeginning());
  }

  @Test
  public void mapEntityToDto_shouldMapDependencies() {
    String expected = "testDependencies";
    topicDraft.setDependencies(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void mapEntityToDto_shouldMapResources() {
    String expected = "testResources";
    topicDraft.setResources(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getResources());
  }

  @Test
  public void mapEntityToDto_shouldMapHandoverPlan() {
    String expected = "testHandoverPlan";
    topicDraft.setHandoverPlan(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected, actual.getHandoverPlan());
  }

  @Test
  public void mapEntityToDto_shouldMapStartTeam() {
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
    OkrUnit expected = new OkrBranch();
    expected.setId(15L);
    topicDraft.setParentUnit(expected);

    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertEquals(expected.getId(), actual.getOkrParentUnitId());
  }

  @Test
  public void mapEntityToDto_shouldCheckIfIsInitiatorIsSet() {
    OkrTopicDraftDto actual = topicDraftMapper.mapEntityToDto(topicDraft);

    assertSame(user, actual.getInitiator());
  }

  @Test
  public void mapEntityToDto_shouldVerifyFindById() {
    topicDraftMapper.mapEntityToDto(topicDraft);

    verify(userService).findById(any());
  }

  @Test
  public void mapEntityToDto_shouldMapCurrentStatus() {
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

  //TODO (F. L. 26.06.2023) fix this test
//  @Test
//  public void mapDtoToEntity_shouldMapBeginning() {
//    LocalDate expected = LocalDate.of(2020, 3, 1);
//    topicDraftDto.setBeginning(expected.toString());
//
//    OkrTopicDraft actual = topicDraftMapper.mapDtoToEntity(topicDraftDto);
//
//    assertEquals(expected, actual.getBeginning());
//  }

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
}
