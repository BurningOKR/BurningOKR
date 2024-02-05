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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.util.ArrayList;
import java.util.UUID;

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

  private OkrCompany company;
  private OkrBranch branch;
  private OkrBranchDto subBranchDto;
  private OkrDepartmentDto departmentDto;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

    this.company = createOkrCompany();
    this.branch = createOkrBranch(this.company);
    this.departmentDto = createDepartmentDto(this.branch);

    this.subBranchDto = new OkrBranchDto();
    subBranchDto.set__okrUnitType(UnitType.OKR_BRANCH);
    subBranchDto.setOkrUnitId(520L);
    subBranchDto.setLabel("Linking a sub branch to a master branch for integration testing");
    subBranchDto.setParentUnitId(this.branch.getId());
    subBranchDto.setIsParentUnitABranch(true);
    subBranchDto.setUnitName("Integration testing");
    subBranchDto.setObjectiveIds(new ArrayList<>());
  }

  @Test
  void addSubDepartmentToBranch_shouldReturn200_whenDepartmentDtoIsValid() throws Exception {
    this.mockMvc.perform(post("/api/branch/{unitId}/department", branch.getId())
        .content(new ObjectMapper().writeValueAsString(this.departmentDto))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isOk());
  }

  @Test
  void addSubDepartmentToBranch_shouldReturn400_whenDepartmentDtoIsInvalid() throws Exception {
    this.departmentDto.setUnitName(null);

    this.mockMvc.perform(post("/api/branch/{unitId}/department", branch.getId())
        .content(new ObjectMapper().writeValueAsString(this.departmentDto))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
  }

  @Test
  void addSubBranchToBranch_shouldReturn200_whenSubBranchDtoIsValid() throws Exception {
    this.mockMvc.perform(post("/api/branch/{unitId}/branch", this.branch.getId())
            .content(new ObjectMapper().writeValueAsString(this.subBranchDto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk());
  }

  @Test
  void addSubBranchToBranch_shouldReturn400_whenSubBranchDtoIsInvalid() throws Exception {
    this.subBranchDto.setUnitName(null);

    this.mockMvc.perform(post("/api/branch/{unitId}/branch", this.branch.getId())
        .content(new ObjectMapper().writeValueAsString(this.subBranchDto))
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isBadRequest());
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



