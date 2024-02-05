package org.burningokr.integration.controller.okrUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.burningokr.controller.okrUnit.CompanyController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.OkrUnitDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.burningokr.utils.Base64Strings;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CompanyController.class})
class CompanyControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private CompanyService companyService;
  @MockBean
  private DataMapper<OkrCompany, OkrCompanyDto> companyMapper;
  @MockBean
  private DataMapper<Cycle, CycleDto> cycleMapper;
  @MockBean
  private DataMapper<OkrBranch, OkrBranchDto> okrBranchMapper;
  @MockBean
  private DataMapper<OkrDepartment, OkrDepartmentDto> okrDepartmentMapper;
  @MockBean
  private OkrBranchSchemaMapper okrUnitSchemaMapper;
  @MockBean
  private AuthenticationUserContextService authenticationUserContextService;
  private OkrCompanyDto okrCompanyDto;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders
      .webAppContextSetup(this.webApplicationContext)
      .build();

    okrCompanyDto = new OkrCompanyDto(
      OkrUnitDto.builder()
        .okrUnitId(100L)
        .unitName("Unit Name")
        .photo(Base64Strings.WHITE_IMAGE_1X1_PIXEL)
        .label("A Label")
        .objectiveIds(new ArrayList<Long>())
        .build()
    );
    okrCompanyDto.setCycleId(98L);
    okrCompanyDto.setHistoryId(99L);
  }

  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(webApplicationContext.getBean(CompanyController.class));
  }

  @Test
  void addCompany_shouldReturnStatus200_whenCompanyDtoIsValid() throws Exception {
    this.mockMvc
      .perform(post("/api/companies")
        .content(new ObjectMapper().writeValueAsString(okrCompanyDto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void addCompany_shouldReturnStatus400_whenCompanyDtoUnitNameIsTooShort() throws Exception {
    okrCompanyDto.setUnitName("");

    this.mockMvc
      .perform(post("/api/companies")
        .content(new ObjectMapper().writeValueAsString(okrCompanyDto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  void addCompany_shouldReturnStatus400_whenCompanyDtoPhotoIsTooBig() throws Exception {
    okrCompanyDto.setPhoto(StringUtils.repeat(Base64Strings.IMAGE_67_BYTE, 10));

    this.mockMvc
      .perform(post("/api/companies")
        .content(new ObjectMapper().writeValueAsString(okrCompanyDto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  void addCompany_shouldReturnStatus400_whenCompanyDtoLabelIsTooShort() throws Exception {
    okrCompanyDto.setLabel("");

    this.mockMvc
      .perform(post("/api/companies")
        .content(new ObjectMapper().writeValueAsString(okrCompanyDto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  void updateCompanyById_shouldReturnStatus200_whenCompanyDtoCompanyDtoIsValid() throws Exception {
    OkrCompany companyMock = mock(OkrCompany.class);
    doNothing().when(companyMock).setId(any());
    doReturn(companyMock).when(this.companyMapper).mapDtoToEntity(this.okrCompanyDto);

    this.mockMvc
      .perform(put("/api/companies/{companyId}", this.okrCompanyDto.getOkrUnitId())
        .content(new ObjectMapper().writeValueAsString(okrCompanyDto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }


  @Test
  void updateCompanyById_shouldReturnStatus400_whenCompanyDtoLabelIsNull() throws Exception {
    this.okrCompanyDto.setLabel(null);

    this.mockMvc
      .perform(put("/api/companies/{companyId}", this.okrCompanyDto.getOkrUnitId())
        .content(new ObjectMapper().writeValueAsString(okrCompanyDto))
        .contentType(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }
}
