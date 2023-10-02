package org.burningokr.integration.controller.okrUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okrUnit.CompanyController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.UnitType;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.repositories.okrUnit.CompanyRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.HandlerMethod;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindingResult;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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


  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.webApplicationContext)
        .build();
  }

  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(webApplicationContext.getBean(CompanyController.class));
  }

  @Test
  void addCompany_shouldCheckIdDtoIsValid() throws Exception {
    OkrCompanyDto companyDto = createCompanyDto();
    companyDto.setPhoto(Base64Strings.WHITE_IMAGE_1X1_PIXEL);
    companyDto.setLabel("a");
    companyDto.setUnitName("unit Name");

    MvcResult result =
        this.mockMvc
            .perform(post("/api/companies")
                .content(new ObjectMapper().writeValueAsString(companyDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertNotNull(companyDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void addCompany_shouldCheckIdDtoIsNotValid() throws Exception {
    OkrCompanyDto companyDto = createCompanyDto();
    companyDto.setLabel("");
    companyDto.setUnitName("");
    companyDto.setHistoryId(-150L);

    MvcResult result =
        this.mockMvc
            .perform(post("/api/companies")
                .content(new ObjectMapper().writeValueAsString(companyDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(companyDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addDepartmentToCompanyById_shouldCheckIfDtoIsValid() throws Exception {
    OkrCompanyDto companyDto = createCompanyDto();


    final OkrDepartmentDto okrDepartment = new OkrDepartmentDto();
    okrDepartment.setOkrUnitId(410L);
    okrDepartment.setLabel("a label");
    okrDepartment.setUnitName("a team");
    okrDepartment.setPhoto(Base64Strings.WHITE_IMAGE_1X1_PIXEL);
    okrDepartment.setObjectiveIds(new ArrayList<>());
    okrDepartment.setOkrMemberIds(new ArrayList<>());
    okrDepartment.set__okrUnitType(UnitType.DEPARTMENT);
    okrDepartment.setParentUnitId(createCompanyDto().getOkrUnitId());


    MvcResult result = this.mockMvc.perform(
            post("/api/companies/{companyId}/departments", companyDto.getOkrUnitId())
                .content(new ObjectMapper().writeValueAsString(okrDepartment))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertNotNull(result);
    assertNotNull(okrDepartment);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void addDepartmentToCompanyById_shouldCheckIfDtoIsNotValid() throws Exception {
    OkrCompanyDto companyDto = createCompanyDto();

    OkrDepartmentDto okrDepartment = new OkrDepartmentDto();
    okrDepartment.setOkrUnitId(410L);
//    okrDepartment.set__okrUnitType(null);
//    okrDepartment.setLabel("");
//    okrDepartment.setUnitName("");
    okrDepartment.setObjectiveIds(new ArrayList<>());


    MvcResult result = this.mockMvc.perform(
            post("/api/companies/{companyId}/departments", companyDto.getOkrUnitId())
                .content(new ObjectMapper().writeValueAsString(okrDepartment))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(result);
    assertNotNull(okrDepartment);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }


  @Test
  void addBranchToCompanyById_shouldCheckIfAValidDTOIsGiven() throws Exception {
    OkrCompanyDto company = createCompanyDto();

    OkrBranchDto okrBranchDto = new OkrBranchDto();
    okrBranchDto.setOkrUnitId(500L);
    okrBranchDto.setLabel("Test Branch");
    okrBranchDto.setUnitName("Unit Name");
    okrBranchDto.setPhoto(Base64Strings.WHITE_IMAGE_1X1_PIXEL);
    okrBranchDto.setObjectiveIds(new ArrayList<>());
    okrBranchDto.setOkrChildUnitIds(new ArrayList<>());
    okrBranchDto.setParentUnitId(company.getOkrUnitId());

    MvcResult result = this.mockMvc.perform(post("/api/companies/{companyId}/branch", company.getOkrUnitId())
            .content(new ObjectMapper().writeValueAsString(okrBranchDto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void addBranchToCompanyById_shouldCheckIfANonValidDTOIsGiven() throws Exception {
    OkrCompanyDto company = createCompanyDto();

    OkrBranchDto okrBranchDto = new OkrBranchDto();
    okrBranchDto.setOkrUnitId(500L);
    okrBranchDto.setLabel("Test Branch");
    okrBranchDto.setObjectiveIds(new ArrayList<>());

    MvcResult result = this.mockMvc.perform(post("/api/companies/{companyId}/branch", company.getOkrUnitId())
            .content(new ObjectMapper().writeValueAsString(okrBranchDto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateCompanyById_shouldCheckIfAValidDTOIsGiven() throws Exception { //todo
    OkrCompanyDto company = createCompanyDto();
    company.setLabel("update label");
    company.setUnitName("update name");

    MvcResult result =
        this.mockMvc.perform(put("/api/companies/{companyId}", company.getOkrUnitId())
                .content(new ObjectMapper().writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertNotNull(company);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateCompanyById_shouldCheckIfANonValidDTOIsGiven() throws Exception { //todo
    OkrCompanyDto company = createCompanyDto();
    company.setLabel("");
    company.setUnitName("");
    company.setCycleId(-800L);
    company.setHistoryId(-123L);

    MvcResult result =
        this.mockMvc.perform(put("/api/companies/{companyId}", company.getOkrUnitId())
                .content(new ObjectMapper().writeValueAsString(company))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(company);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void deleteCompany() { //todo
  }

  private OkrCompanyDto createCompanyDto() {
    OkrCompanyDto company = new OkrCompanyDto();
    company.setOkrUnitId(100L);
    company.setObjectiveIds(new ArrayList<>());
    company.setOkrChildUnitIds(new ArrayList<>());
    return company;
  }


}
