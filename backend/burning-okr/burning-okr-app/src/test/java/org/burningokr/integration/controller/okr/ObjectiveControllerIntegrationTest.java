package org.burningokr.integration.controller.okr;

import jakarta.servlet.ServletContext;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.burningokr.controller.okr.ObjectiveController;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.NoteObjectiveMapper;
import org.burningokr.mapper.okr.ObjectiveMapper;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.repositories.okr.ObjectiveRepository;
import org.burningokr.repositories.okrUnit.CompanyRepository;
import org.burningokr.repositories.okrUnit.OkrUnitRepository;
import org.burningokr.service.okr.ObjectiveService;
import org.burningokr.service.okrUnit.CompanyService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {ObjectiveController.class})
class ObjectiveControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext applicationContext;

  @MockBean
  private ObjectiveService objectiveService;
  @MockBean
  private ObjectiveMapper objectiveMapper;
  @MockBean
  private KeyResultMapper keyResultMapper;
  @MockBean
  private NoteObjectiveMapper noteObjectiveMapper;

  @MockBean
  private CompanyService companyService;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
  }

  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = applicationContext.getServletContext();

    Assertions.assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    Assertions.assertNotNull(applicationContext.getBean(ObjectiveController.class));
  }

  /*@Test
  void getObjectiveById() throws Exception {
    OkrCompany company = new OkrCompany();
    company.setId(800L);

    OkrChildUnit unit = new OkrDepartment();
    unit.setId(805L);

    Objective objective = new Objective();
    objective.setId(810L);
    objective.setName("Testing Entity");

    unit.setObjectives(List.of(objective));
    company.setOkrChildUnits(List.of(unit));

    companyRepository.save(company);
    okrUnitRepository.save(unit);
    objectiveRepository.save(objective);

    MvcResult result = this.mockMvc.perform(get("/api/objectives/{objectiveId}", objective.getId())
            .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())

        .andReturn();

    assertNotNull(result);
    assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  } */

  @Test
  void getKeyResultsOfObjective() {


  }

  @Test
  void updateObjectiveById() {
  }

  @Test
  void getNotesOfObjective() {
  }

  @Test
  void getChildsOfObjective() {
  }

  @Test
  void addKeyResultToObjective() {
  }

  @Test
  void deleteObjectiveById() {
  }

  @Test
  void updateObjectiveKeyResult() {
  }

  @Test
  void addNoteToObjective() {
  }

}
