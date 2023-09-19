package org.burningokr.integration.controller.okrUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okr.KeyResultController;
import org.burningokr.controller.okrUnit.OkrBranchController;
import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.UnitType;
import org.burningokr.mapper.okrUnit.OkrBranchMapper;
import org.burningokr.mapper.okrUnit.OkrDepartmentMapper;
import org.burningokr.model.okr.OkrTopicDescription;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.okrUnits.okrUnitHistories.OkrDepartmentHistory;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.service.okrUnit.CompanyService;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
//@AutoConfigureMockMvc
//@WebAppConfiguration
@ContextConfiguration(classes = {OkrBranchController.class})
class OkrBranchControllerIntegrationTest {
//  @Autowired
//  private MockMvc mockMvc;
//
//  @Autowired
//  private WebApplicationContext webApplicationContext;

  @Autowired
  private OkrBranchController okrBranchController;

  @MockBean
  private OkrChildUnitService<OkrBranch> okrBranchService;
  @MockBean
  private OkrDepartmentMapper departmentMapper;
  @MockBean
  private OkrBranchMapper okrBranchMapper;
  @MockBean
  private CompanyService okrCompanyService;

//  @BeforeEach
//  void setUp() {
//    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
//  }


  @Test
  void addSubDepartmentToBranch() throws Exception {
    final OkrCompany company = new OkrCompany();
    company.setId(500L);
    okrCompanyService.createCompany(company);
//    companyRepository.save(company);


    final OkrBranch okrBranch = new OkrBranch();
    okrBranch.setId(508L);
    okrBranch.setParentOkrUnit(company);
    okrBranchService.createChildUnit(company.getId(), okrBranch);

    final OkrDepartmentDto department = new OkrDepartmentDto();
    department.setOkrUnitId(510L);
    department.set__okrUnitType(UnitType.DEPARTMENT);
    department.setOkrMasterId(UUID.randomUUID());
    department.setUnitName("Department for Integration Testing");
    department.setLabel("Label for department to test");
    department.setParentUnitId(okrBranch.getId());
    department.setIsParentUnitABranch(true);

//    ResponseEntity<OkrDepartmentDto> responseValue = okrBranchController.addSubDepartmentToBranch(okrBranch.getId(), department);

//    MvcResult result = this.mockMvc.perform(post("/api/branch/{unitId}/department", okrBranch.getId())
//            .content(new ObjectMapper().writeValueAsString(department))
//            .contentType(MediaType.APPLICATION_JSON)
//            .accept(MediaType.APPLICATION_JSON))
//        .andExpect(jsonPath("$.id").value(department.getOkrUnitId()))
//        .andExpect(jsonPath("$.name").value(department.getUnitName()))
//        .andExpect(jsonPath("$.label").value(department.getLabel()))
//        .andDo(print())
//        .andExpect(status().isOk())
//        .andReturn();

//    Assertions.assertNotNull(result);
    Assertions.assertNotNull(company);
    Assertions.assertNotNull(okrBranch);
    Assertions.assertNotNull(department);
    Assertions.assertEquals(510L, department.getOkrUnitId());
    Assertions.assertEquals(HttpStatus.OK, responseValue.getStatusCode());

//    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void addSubBranchToBranch() {
  }
}


