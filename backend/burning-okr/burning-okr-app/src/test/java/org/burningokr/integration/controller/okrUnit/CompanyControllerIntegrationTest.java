package org.burningokr.integration.controller.okrUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import jakarta.validation.constraints.AssertTrue;
import org.apache.poi.ss.formula.functions.T;
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
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {CompanyController.class})
@WebAppConfiguration
@WebMvcTest
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
  private OkrBranchSchemaMapper okrUnitSchemaMapper;
  @MockBean
  private AuthenticationUserContextService authenticationUserContextService;
  @MockBean
  private CompanyRepository companyRepository;


  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.webApplicationContext)
//        .apply(springSecurity())
        .build();
  }

  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    Assertions.assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    Assertions.assertNotNull(webApplicationContext.getBean(CompanyController.class));
  }

  @Test
  void getActiveCompanies_shouldReturnOkStatus() throws Exception {
    MvcResult mvcResult =
        this.mockMvc.perform(get("/api/companies"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

  @Test
  void getAllCompanies_shouldReturnOkStatus() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/api/companies/all"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

  @Test
  void getCompanyById() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/api/companies/229"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
//    Assertions.assertTrue(mvcResult.getResponse().equals(HttpStatus.OK));
  }

  @Test
  void getCompanyHistoryByCompanyId() {

  }

  @Test
  void getCompanyCycleList() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/api/companies/{companyId}/cycles", 229))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

  @Test
  void getDepartmentSchemaOfCompany() throws Exception {
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/api/companies/{companyId}/unit", 229))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

  @Test
  @PreAuthorize(value = "@authorizationService.isAdmin()")
  void addCompany() throws Exception {

    OkrCompanyDto companyDto = new OkrCompanyDto();
    companyDto.setOkrUnitId(400L);
    companyDto.setUnitName("Brockhaus AG");
    companyDto.setLabel("Integration Testing");
    companyDto.setObjectiveIds(new ArrayList<>());
    companyDto.setCycleId(399L);
    companyDto.setHistoryId(398L);
    companyDto.setOkrChildUnitIds(new ArrayList<>());

    MvcResult mvcResult =
        this.mockMvc
            .perform(post("/api/companies")
                .content(new ObjectMapper().writeValueAsString(companyDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    Assertions.assertNotNull(mvcResult);
    Assertions.assertNotNull(companyDto);
    Assertions.assertNotEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

  @Test
  void addDepartmentToCompanyById() throws Exception {
    final OkrDepartmentDto okrDepartment = new OkrDepartmentDto();
    okrDepartment.setOkrUnitId(410L);
    okrDepartment.setLabel("Integration Testing");
    okrDepartment.setUnitName("Brockhaus AG Team");
    okrDepartment.setObjectiveIds(new ArrayList<>());
    okrDepartment.setOkrMemberIds(new ArrayList<>());
    okrDepartment.set__okrUnitType(UnitType.DEPARTMENT);
    okrDepartment.setParentUnitId(329L);


    MvcResult result = this.mockMvc.perform(post("/api/companies/{companyId}/departments", 329)
            .content(new ObjectMapper().writeValueAsString(okrDepartment))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(okrDepartment);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }


  @Test
  void addBranchToCompanyById() throws Exception {
    OkrCompany company = new OkrCompany();
    doReturn(company).when(companyService).getAllCompanies();

    OkrBranchDto okrBranchDto = new OkrBranchDto();
    okrBranchDto.setOkrUnitId(500L);
    okrBranchDto.setLabel("Test Branch");
    okrBranchDto.setObjectiveIds(new ArrayList<>());
    okrBranchDto.setOkrChildUnitIds(new ArrayList<>());
    okrBranchDto.setParentUnitId(company.getId());

    MvcResult result = this.mockMvc.perform(post("/api/companies/{companyId}/branch", company.getId())
            .content(new ObjectMapper().writeValueAsString(okrBranchDto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

  }

  @Test
  void updateCompanyById() { //todo
  }

  @Test
  void deleteCompany() { //todo
  }

  private MvcResult runPostIntegrationTest_shouldReturnIsOK(final String path, final int pathId, final T dto) throws Exception {
    return this.mockMvc.perform(post(path, pathId)
            .content(new ObjectMapper().writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }

  private MvcResult runPostIntegrationTest_shouldReturnIsOK(final String path, final T dto) throws Exception {
    return this.mockMvc.perform(post(path)
            .content(new ObjectMapper().writeValueAsString(dto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();
  }
}

