package org.burningokr.mapper.okr;

import org.burningokr.dto.okr.KeyResultMilestoneDto;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.KeyResultMilestone;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collection;

public class KeyResultMilestoneMapperTest {
  private KeyResultMilestone keyResultMilestone;
  private KeyResultMilestoneDto keyResultMilestoneDto;
  private KeyResultMilestoneMapper keyResultMilestoneMapper;

  @Before
  public void init() {
    this.keyResultMilestone = new KeyResultMilestone();
    KeyResult parentKeyResult = new KeyResult();
    this.keyResultMilestone.setParentKeyResult(parentKeyResult);
    this.keyResultMilestoneDto = new KeyResultMilestoneDto();
    this.keyResultMilestoneMapper = new KeyResultMilestoneMapper();
  }

  @Test
  public void test_mapEntityToDto_expectsIdIsMapped() {
    Long expected = 15L;
    keyResultMilestone.setId(expected);
    keyResultMilestoneDto = keyResultMilestoneMapper.mapEntityToDto(keyResultMilestone);
    Assert.assertEquals(expected, keyResultMilestoneDto.getId());
  }

  @Test
  public void test_mapEntityToDto_expectNameIsMapped() {
    String expected = "Name";
    keyResultMilestone.setName(expected);
    keyResultMilestoneDto = keyResultMilestoneMapper.mapEntityToDto(keyResultMilestone);
    Assert.assertEquals(expected, keyResultMilestoneDto.getName());
  }

  @Test
  public void test_mapEntityToDto_expectValueIsMapped() {
    Long expected = 11L;
    keyResultMilestone.setValue(expected);
    keyResultMilestoneDto = keyResultMilestoneMapper.mapEntityToDto(keyResultMilestone);
    Assert.assertEquals(expected, keyResultMilestoneDto.getValue());
  }

  @Test
  public void test_mapEntityToDto_expectParentKeyResultIdIsMapped() {
    Long expected = 18L;
    keyResultMilestone.getParentKeyResult().setId(expected);
    keyResultMilestoneDto = keyResultMilestoneMapper.mapEntityToDto(keyResultMilestone);
    Assert.assertEquals(expected, keyResultMilestoneDto.getParentKeyResultId());
  }

  @Test
  public void test_mapDtoToEntity_expectsIdIsMapped() {
    Long expected = 15L;
    keyResultMilestoneDto.setId(expected);
    keyResultMilestone = keyResultMilestoneMapper.mapDtoToEntity(keyResultMilestoneDto);
    Assert.assertEquals(expected, keyResultMilestone.getId());
  }

  @Test
  public void test_mapDtoToEntity_expectNameIsMapped() {
    String expected = "Name";
    keyResultMilestoneDto.setName(expected);
    keyResultMilestone = keyResultMilestoneMapper.mapDtoToEntity(keyResultMilestoneDto);
    Assert.assertEquals(expected, keyResultMilestone.getName());
  }

  @Test
  public void test_mapDtoToEntity_expectValueIsMapped() {
    Long expected = 11L;
    keyResultMilestoneDto.setValue(expected);
    keyResultMilestone = keyResultMilestoneMapper.mapDtoToEntity(keyResultMilestoneDto);
    Assert.assertEquals(expected, keyResultMilestone.getValue());
  }

  @Test
  public void test_mapDtoToEntity_expectParentKeyResultIdIsMapped() {
    Long expected = 18L;
    keyResultMilestoneDto.setParentKeyResultId(expected);
    keyResultMilestone = keyResultMilestoneMapper.mapDtoToEntity(keyResultMilestoneDto);
    Assert.assertEquals(expected, keyResultMilestone.getParentKeyResult().getId());
  }

  @Test
  public void test_mapEntitiesToDtos_expectEmptyListIsMapped() {
    Collection<KeyResultMilestone> emptyList = new ArrayList<>();
    Collection<KeyResultMilestoneDto> emptyDtoList =
      keyResultMilestoneMapper.mapEntitiesToDtos(emptyList);
    Assert.assertTrue(emptyDtoList.isEmpty());
  }

  @Test
  public void test_mapEntitiesToDtos_expectListIsMapped() {
    Long expected = 10L;
    Collection<KeyResultMilestone> list = new ArrayList<>();
    keyResultMilestone.setId(expected);
    list.add(keyResultMilestone);
    Collection<KeyResultMilestoneDto> dtoList = keyResultMilestoneMapper.mapEntitiesToDtos(list);
    Assert.assertFalse(dtoList.isEmpty());
    Assert.assertTrue(dtoList.stream().anyMatch(dto -> dto.getId().equals(expected)));
  }

  @Test
  public void test_mapDtosToEntities_expectEmptyListIsMapped() {
    Collection<KeyResultMilestoneDto> emptyList = new ArrayList<>();
    Collection<KeyResultMilestone> emptyDtoList =
      keyResultMilestoneMapper.mapDtosToEntities(emptyList);
    Assert.assertTrue(emptyDtoList.isEmpty());
  }

  @Test
  public void test_mapDtosToEntities_expectListIsMapped() {
    Long expected = 10L;
    Collection<KeyResultMilestoneDto> list = new ArrayList<>();
    keyResultMilestoneDto.setId(expected);
    list.add(keyResultMilestoneDto);
    Collection<KeyResultMilestone> dtoList = keyResultMilestoneMapper.mapDtosToEntities(list);
    Assert.assertFalse(dtoList.isEmpty());
    Assert.assertTrue(dtoList.stream().anyMatch(dto -> dto.getId().equals(expected)));
  }
}
