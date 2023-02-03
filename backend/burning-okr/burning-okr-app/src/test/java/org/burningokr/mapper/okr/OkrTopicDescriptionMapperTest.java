package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.model.okr.OkrTopicDescription;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class OkrTopicDescriptionMapperTest {

  private OkrTopicDescription description;
  private OkrTopicDescriptionDto descriptionDto;
  private OkrTopicDescriptionMapper mapper;

  @Before
  public void setUp() {
    description = new OkrTopicDescription();
    descriptionDto = new OkrTopicDescriptionDto();
    mapper = new OkrTopicDescriptionMapper();
  }

  // Entity To Dto

  @Test
  public void test_mapEntityToDto_expect_id_ismapped() {
    description.setId(10l);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(10L, actual.getId().longValue());
  }

  @Test
  public void test_mapEntityToDto_expect_name_ismapped() {
    String expected = "test";

    description.setName(expected);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(expected, actual.getName());
  }

  @Test
  public void test_mapEntityToDto_expect_initiatorId_ismapped() {
    UUID initiatorId = UUID.randomUUID();

    description.setInitiatorId(initiatorId);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(initiatorId, actual.getInitiatorId());
  }

  @Test
  public void test_mapEntityToDto_expect_acceptanceCriteria_ismapped() {
    String expected = "testAcceptanceCriteria";

    description.setDescription(expected);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void test_mapEntityToDto_expect_contributesTo_ismapped() {
    String expected = "testContributesTo";

    description.setContributesTo(expected);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void test_mapEntityToDto_expect_delimitation_ismapped() {
    String expected = "testDelimitation";

    description.setDelimitation(expected);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(expected, actual.getDelimitation());
  }

  @Test
  public void test_mapEntityToDto_expect_beginning_ismapped() {
    LocalDate beginning = LocalDate.of(2020, 3, 1);

    description.setBeginning(beginning);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(beginning, actual.getBeginning());
  }

  @Test
  public void test_mapEntityToDto_expect_dependencies_ismapped() {
    String expected = "testDependencies";

    description.setDependencies(expected);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void test_mapEntityToDto_expect_resources_ismapped() {
    String expected = "testResources";

    description.setResources(expected);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertEquals(expected, actual.getResources());
  }

  @Test
  public void test_mapEntityToDto_expect_handoverPlan_ismapped() {
    String expected = "testHandoverPlan";

    description.setHandoverPlan(expected);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
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

    description.setStartTeam(members);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
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

    description.setStakeholders(members);
    OkrTopicDescriptionDto actual = mapper.mapEntityToDto(description);
    assertTrue(actual.getStakeholders().contains(member1));
    assertTrue(actual.getStakeholders().contains(member2));
    assertTrue(actual.getStakeholders().contains(member3));
  }

  // Dto to Entity

  @Test
  public void test_mapDtoToEntity_expect_id_ismapped() {
    descriptionDto.setId(10l);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(10L, actual.getId().longValue());
  }

  @Test
  public void test_mapDtoToEntity_expect_name_ismapped() {
    String expected = "test";

    descriptionDto.setName(expected);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(expected, actual.getName());
  }

  @Test
  public void test_mapDtoToEntity_expect_initiatorId_ismapped() {
    UUID initiatorId = UUID.randomUUID();

    descriptionDto.setInitiatorId(initiatorId);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(initiatorId, actual.getInitiatorId());
  }

  @Test
  public void test_mapDtoToEntity_expect_acceptanceCriteria_ismapped() {
    String expected = "testAcceptanceCriteria";

    descriptionDto.setDescription(expected);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(expected, actual.getDescription());
  }

  @Test
  public void test_mapDtoToEntity_expect_contributesTo_ismapped() {
    String expected = "testContributesTo";

    descriptionDto.setContributesTo(expected);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(expected, actual.getContributesTo());
  }

  @Test
  public void test_mapDtoToEntity_expect_delimitation_ismapped() {
    String expected = "testDelimitation";

    descriptionDto.setDelimitation(expected);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(expected, actual.getDelimitation());
  }

  @Test
  public void test_mapDtoToEntity_expect_beginning_ismapped() {
    LocalDate beginning = LocalDate.of(2020, 3, 1);

    descriptionDto.setBeginning(beginning);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(beginning, actual.getBeginning());
  }

  @Test
  public void test_mapDtoToEntity_expect_dependencies_ismapped() {
    String expected = "testDependencies";

    descriptionDto.setDependencies(expected);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(expected, actual.getDependencies());
  }

  @Test
  public void test_mapDtoToEntity_expect_resources_ismapped() {
    String expected = "testResources";

    descriptionDto.setResources(expected);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertEquals(expected, actual.getResources());
  }

  @Test
  public void test_mapDtoToEntity_expect_handoverPlan_ismapped() {
    String expected = "testHandoverPlan";

    descriptionDto.setHandoverPlan(expected);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
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

    descriptionDto.setStartTeam(members);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
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

    descriptionDto.setStakeholders(members);
    OkrTopicDescription actual = mapper.mapDtoToEntity(descriptionDto);
    assertTrue(actual.getStakeholders().contains(member1));
    assertTrue(actual.getStakeholders().contains(member2));
    assertTrue(actual.getStakeholders().contains(member3));
  }

  @Test
  public void test_mapEntitiesToDto() {
    List<OkrTopicDescription> descriptions = new ArrayList<>();
    descriptions.add(new OkrTopicDescription());
    descriptions.add(new OkrTopicDescription());
    descriptions.add(new OkrTopicDescription());

    Collection<OkrTopicDescriptionDto> dtos = mapper.mapEntitiesToDtos(descriptions);
    assertEquals(3, (long) dtos.size());
  }

  @Test
  public void test_mapDtosToEntities() {
    List<OkrTopicDescriptionDto> descriptionDtos = new ArrayList<>();
    descriptionDtos.add(new OkrTopicDescriptionDto());
    descriptionDtos.add(new OkrTopicDescriptionDto());
    descriptionDtos.add(new OkrTopicDescriptionDto());

    Collection<OkrTopicDescription> entities = mapper.mapDtosToEntities(descriptionDtos);
    assertEquals(3, (long) entities.size());
  }
}
