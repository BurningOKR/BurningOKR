package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okr.KeyResultController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.NoteKeyResultMapper;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Unit;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.service.okr.KeyResultService;
import org.jetbrains.annotations.NotNull;
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
import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {KeyResultController.class})
class KeyResultControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext context;

  @MockBean
  private KeyResultService keyResultService;
  @MockBean
  private KeyResultMapper keyResultMapper;
  @MockBean
  private NoteKeyResultMapper noteKeyResultMapper;

  private static final String TEXT_255_CHARACTERS_TESTING_PARAMETER =
      "Unveiling the Unprecedented Advancements in Quantum Computing and Quantum Information Science: " +
          "A Multidisciplinary Journey into Quantum Algorithms, Cryptography, and Quantum Supremacy's Global " +
          "Societal and Industrial Transformations.";


  @BeforeEach
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.context).build();
  }

  @Test
  public void getServletContext_shouldCheckIfContextIsLoaded() {
    ServletContext servlet = context.getServletContext();

    assertNotNull(servlet);
    Assertions.assertTrue(servlet instanceof MockServletContext);
    assertNotNull(context.getBean(KeyResultController.class));
  }

  @Test
  void updateKeyResultById_shouldCheckIfAValidDtoIsGiven() throws Exception {
    KeyResultDto keyResult = createValidKeyResultDto(createNestedParameters());

    MvcResult result =
        this.mockMvc.perform(
                put("/api/keyresults/{keyResultId}", keyResult.getId())
                    .content(new ObjectMapper().writeValueAsString(keyResult))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResult);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateKeyResultById_shouldCheckIfANonValidDtoIsGiven() throws Exception {
    KeyResultDto keyResult = createNonValidKeyResultDto(createNestedParameters());

    MvcResult result =
        this.mockMvc.perform(put("/api/keyresults/{keyResultId}", keyResult.getId())
                .content(new ObjectMapper().writeValueAsString(keyResult))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResult);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addNoteToKeyResult_shouldCheckIfValidDTO() throws Exception {
    KeyResultDto keyResult = createValidKeyResultDto(createNestedParameters());
    NoteKeyResultDto noteResult = createValidNoteKeyResultDto(keyResult);

    MvcResult mvcResult =
        this.mockMvc.perform(
                post("/api/keyresults/{keyresultId}/notes", keyResult.getId())
                    .content(new ObjectMapper()
                        .findAndRegisterModules()
                        .writeValueAsString(noteResult)
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(mvcResult);
    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  void addNoteToKeyResult_shouldCheckIfNonValidDTO() throws Exception {
    KeyResultDto keyResult = createValidKeyResultDto(createNestedParameters());
    NoteKeyResultDto noteResult = createNonValidNoteKeyResultDto(keyResult);

    MvcResult mvcResult =
        this.mockMvc.perform(
                post("/api/keyresults/{keyresultId}/notes", keyResult.getId())
                    .content(new ObjectMapper()
                        .findAndRegisterModules()
                        .writeValueAsString(noteResult)
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(mvcResult);
    assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  void updateNoteKeyResult_shouldCheckIfValidDTO() throws Exception {
    KeyResultDto keyResult = createValidKeyResultDto(createNestedParameters());
    NoteKeyResultDto note = createValidNoteKeyResultDto(keyResult);

    MvcResult mvcResult = this.mockMvc
        .perform(
            put("/api/keyresults/notes")
            .content(
                new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(note)
            )
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    assertNotNull(mvcResult);
    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  void updateNoteKeyResult_shouldCheckIfNonValidDTO() throws Exception {
    KeyResultDto keyResult = createValidKeyResultDto(createNestedParameters());
    NoteKeyResultDto note = createNonValidNoteKeyResultDto(keyResult);

    MvcResult mvcResult = this.mockMvc
        .perform(
            put("/api/keyresults/notes")
                .content(
                    new ObjectMapper()
                        .findAndRegisterModules()
                        .writeValueAsString(note)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(mvcResult);
    assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
  }

  private static Objective createNestedParameters() {
    OkrCompany company = new OkrCompany();
    company.setId(100L);

    OkrBranch branch = new OkrBranch();
    branch.setId(101L);
    branch.setParentOkrUnit(company);

    Objective objective = new Objective();
    objective.setId(102L);
    objective.setParentOkrUnit(branch);
    return objective;
  }

  @NotNull
  private static KeyResultDto createValidKeyResultDto(Objective objective) {
    KeyResultDto keyResult = new KeyResultDto();
    keyResult.setId(103L);
    keyResult.setParentObjectiveId(objective.getId());
    keyResult.setTitle("a title");
    keyResult.setDescription("desc");
    keyResult.setStartValue(1);
    keyResult.setCurrentValue(1);
    keyResult.setTargetValue(10);
    keyResult.setUnit(Unit.NUMBER);
    keyResult.setSequence(2);
    keyResult.setNoteIds(new ArrayList<>());
    keyResult.setKeyResultMilestoneDtos(new ArrayList<>());
    return keyResult;
  }

  @NotNull
  private static KeyResultDto createNonValidKeyResultDto(Objective objective) {
    KeyResultDto keyResult = new KeyResultDto();
    keyResult.setId(103L);
    keyResult.setParentObjectiveId(objective.getId());
    keyResult.setTitle(
        TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
    );
    keyResult.setDescription("");
    keyResult.setStartValue(-4);
    keyResult.setCurrentValue(-5);
    keyResult.setTargetValue(-10);
    keyResult.setUnit(Unit.NUMBER);
    keyResult.setSequence(2);
    keyResult.setNoteIds(new ArrayList<>());
    keyResult.setKeyResultMilestoneDtos(new ArrayList<>());
    return keyResult;
  }

  @NotNull
  private static NoteKeyResultDto createValidNoteKeyResultDto(KeyResultDto keyResult) {
    NoteKeyResultDto noteResult = new NoteKeyResultDto();
    noteResult.setNoteId(106L);
    noteResult.setParentKeyResultId(keyResult.getId());
    noteResult.setDate(LocalDateTime.now());
    noteResult.setUserId(UUID.randomUUID());
    noteResult.setNoteBody("b");
    return noteResult;
  }

  @NotNull
  private static NoteKeyResultDto createNonValidNoteKeyResultDto(KeyResultDto keyResult) {
    NoteKeyResultDto noteResult = new NoteKeyResultDto();
    noteResult.setNoteId(106L);
    noteResult.setParentKeyResultId(keyResult.getId());
    noteResult.setDate(LocalDateTime.now());
    noteResult.setUserId(UUID.randomUUID());
    noteResult.setNoteBody(
        TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
            + TEXT_255_CHARACTERS_TESTING_PARAMETER
    );
    return noteResult;
  }
}
