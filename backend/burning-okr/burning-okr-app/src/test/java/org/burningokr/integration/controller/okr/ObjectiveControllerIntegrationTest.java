package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okr.ObjectiveController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.NoteObjectiveMapper;
import org.burningokr.mapper.okr.ObjectiveMapper;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okr.ObjectiveService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

  private static final String TEXT_255_CHARACTERS_TESTING_PARAMETER =
      "Unveiling the Unprecedented Advancements in Quantum Computing and Quantum Information Science: " +
          "A Multidisciplinary Journey into Quantum Algorithms, Cryptography, and Quantum Supremacy's Global " +
          "Societal and Industrial Transformations.";

  @BeforeEach
  void setUp() {

    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.applicationContext)
        .build();
  }

  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = applicationContext.getServletContext();

    Assertions.assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    Assertions.assertNotNull(applicationContext.getBean(ObjectiveController.class));
  }

  @Test
  void updateObjectiveById_shouldCheckIfDTOIsValid() throws Exception {
    OkrBranch branch = createNestingDtoParameters();
    ObjectiveDto objectiveDto = createObjectiveDTO(branch);
    objectiveDto.setTitle(TEXT_255_CHARACTERS_TESTING_PARAMETER);
    objectiveDto.setDescription("a");
    objectiveDto.setReview("b");
    objectiveDto.setRemark("c");
    objectiveDto.setIsActive(false);

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveById_shouldCheckIfDTOIsNotValid() throws Exception {
    OkrBranch branch = createNestingDtoParameters();
    ObjectiveDto objectiveDto = createObjectiveDTO(branch);
    objectiveDto.setTitle(TEXT_255_CHARACTERS_TESTING_PARAMETER + TEXT_255_CHARACTERS_TESTING_PARAMETER);
    objectiveDto.setDescription(
        TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
    );
    objectiveDto.setReview(
        TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
    );
    objectiveDto.setRemark(
        TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
    );


    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addKeyResultToObjective_shouldCheckIfAValidDTOIsGiven() throws Exception {
    OkrBranch branch = createNestingDtoParameters();
    ObjectiveDto objective = createObjectiveDTO(branch);
    KeyResultDto keyResultDto = new KeyResultDto();
    keyResultDto.setId(180L);
    keyResultDto.setParentObjectiveId(objective.getId());
    keyResultDto.setTitle(TEXT_255_CHARACTERS_TESTING_PARAMETER);
    keyResultDto.setDescription("desc");
    keyResultDto.setStartValue(0);
    keyResultDto.setCurrentValue(1);
    keyResultDto.setTargetValue(10);

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/keyresults", objective.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void addKeyResultToObjective_shouldCheckIfNonValidDTOIsGiven() throws Exception {
    OkrBranch branch = createNestingDtoParameters();
    ObjectiveDto objective = createObjectiveDTO(branch);
    KeyResultDto keyResultDto = new KeyResultDto();
    keyResultDto.setId(180L);
    keyResultDto.setParentObjectiveId(objective.getId());
    keyResultDto.setTitle(
        TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
    );
    keyResultDto.setStartValue(-1);
    keyResultDto.setDescription(
        TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
    );
    keyResultDto.setCurrentValue(-1);
    keyResultDto.setTargetValue(-1);

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/keyresults", objective.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveKeyResult_shouldCheckIfAValidDTOIsGiven() throws Exception {
    final OkrBranch branch = createNestingDtoParameters();
    final ObjectiveDto objective = createObjectiveDTO(branch);
    final NoteObjectiveDto noteDto = createNoteDto(objective);

    MvcResult result =
        this.mockMvc
            .perform(put("/api/objectives/notes")
                .content(new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertNotNull(noteDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveKeyResult_shouldCheckIfANonValidDTOIsGiven() throws Exception {
    final OkrBranch branch = createNestingDtoParameters();
    final ObjectiveDto objective = createObjectiveDTO(branch);
    final NoteObjectiveDto noteDto = createNoteDto(objective);
    noteDto.setNoteBody("");

    MvcResult result =
        this.mockMvc
            .perform(put("/api/objectives/notes")
                .content(new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(noteDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addNoteToObjective_shouldCheckIfAValidDTOIsGiven() throws Exception {
    final OkrBranch branch = createNestingDtoParameters();
    final ObjectiveDto objective = createObjectiveDTO(branch);
    final NoteObjectiveDto noteDto = createNoteDto(objective);
    noteDto.setNoteBody(TEXT_255_CHARACTERS_TESTING_PARAMETER);

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/notes", objective.getId())
                .content(new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertNotNull(noteDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }


  @Test
  void addNoteToObjective_shouldCheckIfANonValidDTOIsGiven() throws Exception {
    final OkrBranch branch = createNestingDtoParameters();
    final ObjectiveDto objective = createObjectiveDTO(branch);
    final NoteObjectiveDto noteDto = createNoteDto(objective);
    noteDto.setNoteBody("");

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/notes", objective.getId())
                .content(new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(noteDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  private static OkrBranch createNestingDtoParameters() {
    OkrCompany company = new OkrCompany();
    company.setId(100L);

    OkrBranch branch = new OkrBranch();
    branch.setId(101L);
    branch.setParentOkrUnit(company);
    branch.setName("Branch for Integration Testing");

    return branch;
  }

  private static ObjectiveDto createObjectiveDTO(OkrBranch branch) {
    ObjectiveDto objectiveDto = new ObjectiveDto();
    objectiveDto.setId(150L);
    objectiveDto.setParentUnitId(branch.getId());
    return objectiveDto;
  }

  private static NoteObjectiveDto createNoteDto(ObjectiveDto objective) {
    final NoteObjectiveDto noteDto = new NoteObjectiveDto();
    noteDto.setNoteId(180L);
    noteDto.setParentObjectiveId(objective.getId());
//    noteDto.setNoteBody(TEXT_255_CHARACTERS_TESTING_PARAMETER);
    noteDto.setDate(LocalDateTime.now());
    return noteDto;
  }

}
