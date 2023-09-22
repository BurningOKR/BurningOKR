package org.burningokr.integration.controller.okrUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.burningokr.controller.okrUnit.OkrBranchController;
import org.burningokr.dto.okrUnit.OkrBranchDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.UnitType;
import org.burningokr.mapper.okrUnit.OkrBranchMapper;
import org.burningokr.mapper.okrUnit.OkrDepartmentMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okrUnit.OkrChildUnitService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.nullable;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@EnableWebMvc
@WebAppConfiguration
@ContextConfiguration(classes = {OkrBranchController.class})
class OkrBranchControllerIntegrationTest {
  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private OkrChildUnitService<OkrBranch> okrBranchService;
  @MockBean
  private OkrDepartmentMapper departmentMapper;
  @MockBean
  private OkrBranchMapper mapper;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
  }

  @Test
  void addSubDepartmentToBranch_shouldCreateNewEntityToDatabase() throws Exception {
    OkrBranch okrBranch = createOkrBranch(createOkrCompany());
    OkrDepartmentDto department = createDepartmentDto(okrBranch);

    MvcResult result = this.mockMvc.perform(post("/api/branch/{unitId}/department", okrBranch.getId())
            .content(new ObjectMapper().writeValueAsString(department))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertNotNull(result);
    assertNotNull(department);
    Assertions.assertEquals(510L, department.getOkrUnitId());
  }

  @Test
  void addSubDepartmentToBranch_shouldCheckIfTheEntityIsValid() throws Exception {
    OkrBranch okrBranch = createOkrBranch(createOkrCompany());
    OkrDepartmentDto department = createDepartmentDto(okrBranch);
    department.setOkrMemberIds(null);

    MvcResult result = this.mockMvc.perform(post("/api/branch/{unitId}/department", okrBranch.getId())
            .content(new ObjectMapper().writeValueAsString(department))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(result);
    assertNotNull(department);
    Assertions.assertNull(department.getOkrMemberIds());
    Assertions.assertNull(result.getResponse().getContentType());
  }

  @Test
  void addSubBranchToBranch_shouldCheckIfABranchCanBeCreated() throws Exception {
    OkrCompany company = createOkrCompany();
    OkrBranch masterBranch = createOkrBranch(company);
    OkrBranchDto subBranch = new OkrBranchDto();
    subBranch.set__okrUnitType(UnitType.OKR_BRANCH);
    subBranch.setOkrUnitId(520L);
    subBranch.setLabel("Linking a sub branch to a master branch for integration testing");
    subBranch.setParentUnitId(masterBranch.getId());
    subBranch.setIsParentUnitABranch(true);
    subBranch.setUnitName("Integration testing");
    subBranch.setObjectiveIds(new ArrayList<>());


    MvcResult result = this.mockMvc.perform(post("/api/branch/{unitId}/branch", masterBranch.getId())
            .content(new ObjectMapper().writeValueAsString(subBranch))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertNotNull(result);
    assertNotNull(company);
    assertNotNull(masterBranch);
    assertNotNull(subBranch);
    assertEquals(520L, subBranch.getOkrUnitId());
  }

  @Test
  void addSubBranchToBranch_shouldCheckIfAValidDtoHasBeenGiven() throws Exception {
    OkrCompany company = createOkrCompany();
    OkrBranch masterBranch = createOkrBranch(company);
    OkrBranchDto subBranch = new OkrBranchDto();
    subBranch.set__okrUnitType(UnitType.OKR_BRANCH);
    subBranch.setOkrUnitId(520L);
    subBranch.setLabel("Linking a sub branch to a master branch for integration testing");
    subBranch.setParentUnitId(masterBranch.getId());
    subBranch.setIsParentUnitABranch(true);
    subBranch.setUnitName("Integration testing");
    subBranch.setOkrChildUnitIds(nullable(ArrayList.class)); //how to check for nullable

    MvcResult result = this.mockMvc.perform(post("/api/branch/{unitId}/branch", masterBranch.getId())
            .content(new ObjectMapper().writeValueAsString(subBranch))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(result);
    assertNotNull(company);
    assertNotNull(masterBranch);
    assertNotNull(subBranch);
    assertNotNull(result.getResponse().getContentType());
    assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  private OkrCompany createOkrCompany() {
    final OkrCompany company = new OkrCompany();
    company.setId(500L);
    return company;
  }

  private OkrBranch createOkrBranch(OkrCompany company) {
    final OkrBranch okrBranch = new OkrBranch();
    okrBranch.setId(508L);
    okrBranch.setParentOkrUnit(company);
    return okrBranch;
  }

  private OkrDepartmentDto createDepartmentDto(final OkrBranch okrBranch) {
    final OkrDepartmentDto department = new OkrDepartmentDto();
    department.setOkrUnitId(510L);
    department.set__okrUnitType(UnitType.DEPARTMENT);
    department.setOkrMasterId(UUID.randomUUID());
    department.setUnitName("Department for Integration Testing");
    department.setLabel("Label for department to test");
    department.setParentUnitId(okrBranch.getId());
    department.setIsParentUnitABranch(true);
    return department;
  }
}



