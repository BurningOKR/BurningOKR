package org.burningokr.integration.controller.okrUnit;

import jakarta.servlet.ServletContext;
import org.burningokr.controller.okrUnit.CompanyController;
import org.burningokr.dto.cycle.CycleDto;
import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.mapper.okrUnit.OkrBranchSchemaMapper;
import org.burningokr.model.cycles.Cycle;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.security.authenticationUserContext.AuthenticationUserContextService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
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

import javax.print.attribute.standard.Media;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
//    MvcResult r = this.mockMvc.perform(get("/api/admin")
//            .with(user("admin").roles("USER","ADMIN")))
//        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
//        .andReturn();

//    r.
    MvcResult mvcResult =
        this.mockMvc
            .perform(get("/api/companies/{companyId}/departments", "229"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

  @Test
  void addDepartmentToCompanyById() { //todo
  }

  @Test
  void addBranchToCompanyById() { ////todo
  }

  @Test
  void updateCompanyById() { //todo
  }

  @Test
  void deleteCompany() { //todo
  }
}

