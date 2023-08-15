package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.service.util.DateMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class OkrTopicDescriptionMapperTest {

  private OkrTopicDescription topicDescription;
  private OkrTopicDescriptionDto topicDescriptionDto;
  private OkrTopicDescriptionMapper topicDescriptionMapper;

  @BeforeEach
  public void setUp() {
    DateMapper dateMapper = new DateMapper();
    topicDescription = new OkrTopicDescription();
    topicDescriptionDto = new OkrTopicDescriptionDto();
    topicDescriptionMapper = new OkrTopicDescriptionMapper(dateMapper);
  }

  @Test
  public void mapEntityToDto_shouldMapId() {
    long expected = 10L;
    topicDescription.setId(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected, actual.getId().longValue());
  }

  @Test
  public void mapEntityToDto_shouldMapExpected() {
    String expected = "test";
    topicDescription.setName(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected, actual.getName());
  }

  @Test
  public void mapEntityToDto_shouldMapInitiatorId() {
    UUID expected = UUID.randomUUID();
    topicDescription.setInitiatorId(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected, actual.getInitiatorId());
  }

  @Test
  public void mapEntityToDto_shouldMapAcceptanceCriteria() {
    String expected = "testAcceptanceCriteria";
    topicDescription.setDescription(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void mapEntityToDto_shouldMapContributesTo() {
    String expected = "testContributesTo";
    topicDescription.setContributesTo(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void mapEntityToDto_shouldMapDelimitation() {
    String expected = "testDelimitation";
    topicDescription.setDelimitation(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected, actual.getDelimitation());
  }

  @Test
  public void mapEntityToDto_shouldMapBeginning() {
    LocalDate expected = LocalDate.of(2020, 3, 1);
    topicDescription.setBeginning(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected.toString(), actual.getBeginning());
  }

  @Test
  public void mapEntityToDto_shouldMapDependencies() {
    String expected = "testDependencies";
    topicDescription.setDependencies(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void mapEntityToDto_shouldMapResources() {
    String expected = "testResources";
    topicDescription.setResources(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertEquals(expected, actual.getResources());
  }

  @Test
  public void mapEntityToDto_shouldMapHandoverPlan() {
    String expected = "testHandoverPlan";
    topicDescription.setHandoverPlan(expected);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

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
    topicDescription.setStartTeam(members);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

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
    topicDescription.setStakeholders(members);

    OkrTopicDescriptionDto actual = topicDescriptionMapper.mapEntityToDto(topicDescription);

    assertTrue(actual.getStakeholders().contains(member1));
    assertTrue(actual.getStakeholders().contains(member2));
    assertTrue(actual.getStakeholders().contains(member3));
  }

  @Test
  public void mapDtoToEntity_shouldMapId() {
    long expected = 10L;
    topicDescriptionDto.setId(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getId().longValue());
  }

  @Test
  public void mapDtoToEntity_shouldMapName() {
    String expected = "test";
    topicDescriptionDto.setName(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getName());
  }

  @Test
  public void mapDtoToEntity_shouldMapInitiatorId() {
    UUID expected = UUID.randomUUID();
    topicDescriptionDto.setInitiatorId(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getInitiatorId());
  }

  @Test
  public void mapDtoToEntity__shouldMapDescription() {
    String expected = "testAcceptanceCriteria";
    topicDescriptionDto.setDescription(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void mapDtoToEntity_shouldMapContributesTo() {
    String expected = "testContributesTo";
    topicDescriptionDto.setContributesTo(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void mapDtoToEntity__shouldMapDelimitation() {
    String expected = "testDelimitation";
    topicDescriptionDto.setDelimitation(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getDelimitation());
  }

  @Test
  public void mapDtoToEntity_shouldMapBeginning() {
    LocalDate expected = LocalDate.of(2020, 3, 1);
    topicDescriptionDto.setBeginning(expected.toString());

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getBeginning());
  }

  @Test
  public void mapDtoToEntity_shouldMapDependencies() {
    String expected = "testDependencies";
    topicDescriptionDto.setDependencies(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void mapDtoToEntity_shouldMapResources() {
    String expected = "testResources";
    topicDescriptionDto.setResources(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertEquals(expected, actual.getResources());
  }

  @Test
  public void mapDtoToEntity_shouldMapHandoverPlan() {
    String expected = "testHandoverPlan";
    topicDescriptionDto.setHandoverPlan(expected);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

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
    topicDescriptionDto.setStartTeam(members);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

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
    topicDescriptionDto.setStakeholders(members);

    OkrTopicDescription actual = topicDescriptionMapper.mapDtoToEntity(topicDescriptionDto);

    assertTrue(actual.getStakeholders().contains(member1));
    assertTrue(actual.getStakeholders().contains(member2));
    assertTrue(actual.getStakeholders().contains(member3));
  }

  @Test
  public void mapEntitiesToDto_shouldMapDescriptionsArray() {
    int expected = 3;
    List<OkrTopicDescription> topicDescription = new ArrayList<>() {
      {
        add(new OkrTopicDescription());
        add(new OkrTopicDescription());
        add(new OkrTopicDescription());
      }
    };

    Collection<OkrTopicDescriptionDto> actual = topicDescriptionMapper.mapEntitiesToDtos(topicDescription);

    assertEquals(expected, actual.size());
  }

  @Test
  public void mapDtosToEntities_shouldMapDescriptionsArray() {
    int expected = 3;
    List<OkrTopicDescriptionDto> topicDescriptionDtos = new ArrayList<>() {
      {
        add(new OkrTopicDescriptionDto());
        add(new OkrTopicDescriptionDto());
        add(new OkrTopicDescriptionDto());
      }
    };

    Collection<OkrTopicDescription> actual = topicDescriptionMapper.mapDtosToEntities(topicDescriptionDtos);

    assertEquals(expected, actual.size());
  }
}
