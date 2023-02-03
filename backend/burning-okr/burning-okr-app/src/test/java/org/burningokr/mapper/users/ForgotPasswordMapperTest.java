package org.burningokr.mapper.users;

import org.burningokr.dto.users.ForgotPasswordDto;
import org.burningokr.model.users.ForgotPassword;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class ForgotPasswordMapperTest {
  private final ForgotPasswordMapper forgotPasswordMapper = new ForgotPasswordMapper();
  private ForgotPasswordDto forgotPasswordDto;
  private ForgotPassword forgotPassword;

  @Before
  public void init() {
    forgotPasswordDto = new ForgotPasswordDto();
    forgotPassword = new ForgotPassword();
  }

  @Test
  public void mapDtoToEntity_expectAllMapped() {
    forgotPasswordDto.setEmail("test@example.com");

    forgotPassword = forgotPasswordMapper.mapDtoToEntity(forgotPasswordDto);

    assertMapped(forgotPasswordDto, forgotPassword);
  }

  @Test
  public void mapEntityToDto_expectAllMapped() {
    forgotPassword.setEmail("test@example.com");

    forgotPasswordDto = forgotPasswordMapper.mapEntityToDto(forgotPassword);

    assertMapped(forgotPassword, forgotPasswordDto);
  }

  @Test
  public void mapDtosToEntities_expectAllMapped() {
    ForgotPasswordDto forgotPasswordDto1 = new ForgotPasswordDto();
    forgotPasswordDto1.setEmail("test1@example.com");
    ForgotPasswordDto forgotPasswordDto2 = new ForgotPasswordDto();
    forgotPasswordDto2.setEmail("test2@example.com");
    ArrayList<ForgotPasswordDto> forgotPasswordDtos = new ArrayList<>(2);
    forgotPasswordDtos.add(forgotPasswordDto1);
    forgotPasswordDtos.add(forgotPasswordDto2);

    ArrayList<ForgotPassword> forgotPasswords =
        (ArrayList<ForgotPassword>) forgotPasswordMapper.mapDtosToEntities(forgotPasswordDtos);

    assertThat(forgotPasswordDtos, hasSize(2));
    assertEquals(forgotPasswordDtos.size(), forgotPasswords.size());
    for (int i = 0; i < forgotPasswordDtos.size(); i++) {
      assertMapped(forgotPasswordDtos.get(i), forgotPasswords.get(i));
    }
  }

  @Test
  public void mapEntitiesToDtos_expectAllMapped() {
    ForgotPassword forgotPassword1 = new ForgotPassword();
    forgotPassword1.setEmail("test1@example.com");
    ForgotPassword forgotPassword2 = new ForgotPassword();
    forgotPassword2.setEmail("test2@example.com");
    ArrayList<ForgotPassword> forgotPasswords = new ArrayList<>(2);
    forgotPasswords.add(forgotPassword1);
    forgotPasswords.add(forgotPassword2);

    ArrayList<ForgotPasswordDto> forgotPasswordDtos =
        (ArrayList<ForgotPasswordDto>) forgotPasswordMapper.mapEntitiesToDtos(forgotPasswords);

    assertThat(forgotPasswords, hasSize(2));
    assertEquals(forgotPasswords.size(), forgotPasswordDtos.size());
    for (int i = 0; i < forgotPasswords.size(); i++) {
      assertMapped(forgotPasswords.get(i), forgotPasswordDtos.get(i));
    }
  }

  private void assertMapped(ForgotPassword expected, ForgotPasswordDto actual) {
    assertEquals(expected.getEmail(), actual.getEmail());
  }

  private void assertMapped(ForgotPasswordDto expected, ForgotPassword actual) {
    assertEquals(expected.getEmail(), actual.getEmail());
  }
}
