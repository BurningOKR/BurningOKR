package org.burningokr.mapper.users;

import org.burningokr.dto.users.ChangePasswordDto;
import org.burningokr.model.users.ChangePasswordData;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ChangePasswordMapperTest {

  private ChangePasswordData changePasswordData;
  private ChangePasswordDto changePasswordDto;
  private ChangePasswordMapper changePasswordMapper;

  @Before
  public void init() {
    changePasswordData = new ChangePasswordData();
    changePasswordDto = new ChangePasswordDto();
    changePasswordMapper = new ChangePasswordMapper();
  }

  @Test
  public void mapEntityToDto_expectAllMapped() {
    changePasswordData.setOldPassword("oldPassword");
    changePasswordData.setNewPassword("newPassword");
    changePasswordData.setEmail("e@mail");

    changePasswordDto = this.changePasswordMapper.mapEntityToDto(changePasswordData);

    assertDtoMappedCorrectly(this.changePasswordData, this.changePasswordDto);
  }

  @Test
  public void mapDtoToEntity_expectAllMapped() {
    changePasswordDto.setOldPassword("oldPassword");
    changePasswordDto.setNewPassword("newPassword");
    changePasswordDto.setEmail("e@mail");

    changePasswordData = this.changePasswordMapper.mapDtoToEntity(changePasswordDto);

    assertEnitityMappedCorrectly(this.changePasswordDto, this.changePasswordData);
  }

  @Test
  public void mapDtosToEntities_expectAllMapped() {
    ArrayList<ChangePasswordDto> dtoList = new ArrayList<>();
    ChangePasswordDto changePasswordDto1 = new ChangePasswordDto();
    changePasswordDto1.setNewPassword("newPassword1");
    changePasswordDto1.setOldPassword("oldPassword1");
    changePasswordDto1.setEmail("e@mail1");
    dtoList.add(changePasswordDto1);
    ChangePasswordDto changePasswordDto2 = new ChangePasswordDto();
    changePasswordDto2.setNewPassword("newPassword2");
    changePasswordDto2.setOldPassword("oldPassword2");
    changePasswordDto2.setEmail("e@mail2");
    dtoList.add(changePasswordDto2);
    ChangePasswordDto changePasswordDto3 = new ChangePasswordDto();
    changePasswordDto3.setNewPassword("newPassword3");
    changePasswordDto3.setOldPassword("oldPassword3");
    changePasswordDto3.setEmail("e@mail3");
    dtoList.add(changePasswordDto3);

    ArrayList<ChangePasswordData> dataList =
      (ArrayList<ChangePasswordData>) this.changePasswordMapper.mapDtosToEntities(dtoList);

    assertEquals(3, dtoList.size());
    assertEquals(dtoList.size(), dataList.size());
    for (int i = 0; i < dataList.size(); i++) {
      assertEnitityMappedCorrectly(dtoList.get(i), dataList.get(i));
    }
  }

  @Test
  public void mapDtosToEntities_expectOutputToBeEmpty() {
    ArrayList<ChangePasswordDto> dtoList = new ArrayList<>();

    ArrayList<ChangePasswordData> dataList =
      (ArrayList<ChangePasswordData>) this.changePasswordMapper.mapDtosToEntities(dtoList);

    assertTrue(dataList.isEmpty());
  }

  @Test
  public void mapEntitysToDtos_expectAllMapped() {
    ArrayList<ChangePasswordData> dataList = new ArrayList<>();
    ChangePasswordData changePasswordData1 = new ChangePasswordData();
    changePasswordData1.setNewPassword("newPassword1");
    changePasswordData1.setOldPassword("oldPassword1");
    changePasswordData1.setEmail("e@mail1");
    ChangePasswordData changePasswordData2 = new ChangePasswordData();
    changePasswordData2.setNewPassword("newPassword2");
    changePasswordData2.setOldPassword("oldPassword2");
    changePasswordData2.setEmail("e@mail2");
    ChangePasswordData changePasswordData3 = new ChangePasswordData();
    changePasswordData3.setNewPassword("newPassword3");
    changePasswordData3.setOldPassword("oldPassword3");
    changePasswordData3.setEmail("e@mail3");

    ArrayList<ChangePasswordDto> dtoList =
      (ArrayList<ChangePasswordDto>) this.changePasswordMapper.mapEntitiesToDtos(dataList);

    for (int i = 0; i < dataList.size(); i++) {
      assertEnitityMappedCorrectly(dtoList.get(i), dataList.get(i));
    }
  }

  @Test
  public void mapEntitysToDtos_expectOutputToBeEmpty() {
    ArrayList<ChangePasswordData> dataList = new ArrayList<>();

    ArrayList<ChangePasswordDto> dtoList =
      (ArrayList<ChangePasswordDto>) this.changePasswordMapper.mapEntitiesToDtos(dataList);

    assertTrue(dtoList.isEmpty());
  }

  private void assertEnitityMappedCorrectly(
    ChangePasswordDto changePasswordDto, ChangePasswordData changePasswordData
  ) {
    assertEquals(changePasswordDto.getEmail(), changePasswordData.getEmail());
    assertEquals(changePasswordDto.getNewPassword(), changePasswordData.getNewPassword());
    assertEquals(changePasswordDto.getOldPassword(), changePasswordData.getOldPassword());
  }

  private void assertDtoMappedCorrectly(
    ChangePasswordData changePasswordData, ChangePasswordDto changePasswordDto
  ) {
    assertEquals(changePasswordData.getEmail(), changePasswordDto.getEmail());
    assertEquals(changePasswordData.getNewPassword(), changePasswordDto.getNewPassword());
    assertEquals(changePasswordData.getOldPassword(), changePasswordDto.getOldPassword());
  }
}
