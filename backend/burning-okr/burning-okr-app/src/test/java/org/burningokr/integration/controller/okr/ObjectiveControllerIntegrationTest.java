package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.burningokr.controller.okr.ObjectiveController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteObjectiveDto;
import org.burningokr.dto.okr.ObjectiveDto;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.NoteObjectiveMapper;
import org.burningokr.mapper.okr.ObjectiveMapper;
import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;
import org.burningokr.model.okr.Unit;
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
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
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

  private ObjectiveDto objectiveDto;
  private KeyResultDto keyResultDto;
  private NoteObjectiveDto noteObjectiveDto;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.applicationContext)
        .build();

    objectiveDto = ObjectiveDto.builder()
        .id(120L)
        .parentUnitId(100L)
        .parentObjectiveId(110L)
        .title("Objective")
        .description("Description")
        .remark("Remark")
        .review("Review")
        .sequence(5)
        .isActive(true)
        .contactPersonId("burning-okr@localhost.loop")
        .subObjectiveIds(new ArrayList<>())
        .keyResultIds(new ArrayList<>())
        .noteIds(new ArrayList<>())
        .build();

    keyResultDto = KeyResultDto.builder()
        .id(150L)
        .parentObjectiveId(objectiveDto.getId())
        .title("Title")
        .description("Description")
        .startValue(1L)
        .currentValue(5L)
        .targetValue(20L)
        .unit(Unit.EURO)
        .sequence(3)
        .noteIds(new ArrayList<>())
        .keyResultMilestoneDtos(new ArrayList<>())
        .build();

    noteObjectiveDto = NoteObjectiveDto.builder()
        .noteId(190L)
        .userId(UUID.randomUUID())
        .noteBody("Note Body")
        .date(LocalDateTime.now())
        .parentObjectiveId(objectiveDto.getId())
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
  void updateObjectiveById_shouldReturnStatus200_whenDtoIsValid() throws Exception {
    Objective objectiveMock = mock(Objective.class);
    doNothing().when(objectiveMock).setId(any());
    doReturn(objectiveMock).when(objectiveMapper).mapDtoToEntity(this.objectiveDto);

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveById_shouldReturnStatus400_whenParentIdIsNull() throws Exception {
    objectiveDto.setParentUnitId(null);

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveById_shouldReturnStatus400_whenTitleIsToLong() throws Exception {
    objectiveDto.setTitle(StringUtils.repeat("A", 256));

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveById_shouldReturnValidationMessage_whenTitleIsToLong() throws Exception {
    objectiveDto.setTitle(StringUtils.repeat("A", 256));

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "The title of an objective may not be empty or longer than 255 characters."));
  }

  @Test
  void updateObjectiveById_shouldReturnStatus400_whenDescriptionIsToLong() throws Exception {
    objectiveDto.setDescription(StringUtils.repeat("A", 1024));

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveById_shouldReturnValidationMessage_whenDescriptionIsToLong() throws Exception {
    objectiveDto.setDescription(StringUtils.repeat("A", 1024));

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "The description of an objective may not be longer than 1023 characters."));
  }

  @Test
  void updateObjectiveById_shouldReturnStatus400_whenRemarkIsToLong() throws Exception {
    objectiveDto.setRemark(StringUtils.repeat("A", 1024));

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveById_shouldReturnValidationMessage_whenRemarkIsToLong() throws Exception {
    objectiveDto.setRemark(StringUtils.repeat("A", 1024));

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "The remark of an objective may not be longer than 1023 characters."));
  }

  @Test
  void updateObjectiveById_shouldReturnStatus400_whenReviewIsToLong() throws Exception {
    objectiveDto.setReview(StringUtils.repeat("A", 2048));

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveById_shouldReturnValidationMessage_whenReviewIsToLong() throws Exception {
    objectiveDto.setReview(StringUtils.repeat("A", 2048));

    MvcResult result = this.mockMvc.perform(
            put("/api/objectives/{objectiveId}", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(objectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
        .andReturn();


    assertNotNull(result);
    assertNotNull(objectiveDto);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(), "The review of an objective may not be longer than 2047 characters."));

  }
  @Test
  void addKeyResultToObjective_shouldReturnStatus200_whenAValidDTOIsGiven() throws Exception {
    KeyResult keyResultMock = mock(KeyResult.class);
    doNothing().when(keyResultMock).setId(any());
    doReturn(keyResultMock).when(keyResultMapper).mapDtoToEntity(this.keyResultDto);

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void addKeyResultToObjective_shouldReturnStatus400_whenKeyResultTitleIsTooLong() throws Exception {
    keyResultDto.setTitle(StringUtils.repeat("a", 256));

    MvcResult result =
      this.mockMvc
        .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
          .content(new ObjectMapper().writeValueAsString(keyResultDto))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addKeyResultToObjective_shouldReturnErrorMessage_whenKeyResultTitleIsTooLong() throws Exception {
    keyResultDto.setTitle(StringUtils.repeat("a", 256));

    MvcResult result =
      this.mockMvc
        .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
          .content(new ObjectMapper().writeValueAsString(keyResultDto))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(), "The title of a key result may not be empty or longer than 255 characters."));
  }

  @Test
  void addKeyResultToObjective_shouldReturnStatus400_whenKeyResultTitleIsEmpty() throws Exception {
    keyResultDto.setTitle("");

    MvcResult result =
      this.mockMvc
        .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
          .content(new ObjectMapper().writeValueAsString(keyResultDto))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addKeyResultToObjective_shouldReturnErrorMessage_whenKeyResultTitleIsEmpty() throws Exception {
    keyResultDto.setTitle("");

    MvcResult result =
      this.mockMvc
        .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
          .content(new ObjectMapper().writeValueAsString(keyResultDto))
          .contentType(MediaType.APPLICATION_JSON)
          .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(), "The title of a key result may not be empty or longer than 255 characters."));
  }

  @Test
  void addKeyResultToObjective_shouldReturnStatus400_whenKeyResultDescriptionIsTooLong() throws Exception {
    keyResultDto.setTitle(StringUtils.repeat("a", 1024));

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addKeyResultToObjective_shouldReturnErrorMessage_whenKeyResultDescriptionIsTooLong() throws Exception {
    keyResultDto.setDescription(StringUtils.repeat("a", 1024));

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "The description of a key result may not be empty or longer than 1023 characters."));
  }

  @Test
  void addKeyResultToObjective_shouldReturnStatus400_whenStartValueIsNegative() throws Exception {
    keyResultDto.setStartValue(-1L);

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addKeyResultToObjective_shouldReturnStatus400_whenCurrentValueIsNegative() throws Exception {
    keyResultDto.setCurrentValue(-2L);

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addKeyResultToObjective_shouldReturnStatus400_whenTargetValueIsNegative() throws Exception {
    keyResultDto.setStartValue(-10L);

    MvcResult result =
        this.mockMvc
            .perform(post("/api/objectives/{objectiveId}/keyresults", objectiveDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveKeyResult_shouldReturnStatus200_whenDtoIsValid() throws Exception {
    MvcResult result =
        this.mockMvc
            .perform(put("/api/objectives/notes")
                .content(new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteObjectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(result);
    assertNotNull(noteObjectiveDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveKeyResult_shouldReturn400_whenNoteBodyIsNull() throws Exception {
    noteObjectiveDto.setNoteBody(null);

    MvcResult result =
        this.mockMvc
            .perform(put("/api/objectives/notes")
                .content(new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteObjectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(result);
    assertNotNull(noteObjectiveDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }


  @Test
  void updateObjectiveKeyResult_shouldReturn400_whenNoteBodyIsTooLong() throws Exception {
    noteObjectiveDto.setNoteBody(StringUtils.repeat("a", 1024));

    MvcResult result =
        this.mockMvc
            .perform(put("/api/objectives/notes")
                .content(new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteObjectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(result);
    assertNotNull(noteObjectiveDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateObjectiveKeyResult_shouldReturnErrorMessage_whenNoteBodyIsTooLong() throws Exception {
    noteObjectiveDto.setNoteBody(StringUtils.repeat("a", 1024));

    MvcResult result =
        this.mockMvc
            .perform(put("/api/objectives/notes")
                .content(new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteObjectiveDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(noteObjectiveDto);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(), "The note text may not be empty or longer than 1023 characters."));
  }


}
