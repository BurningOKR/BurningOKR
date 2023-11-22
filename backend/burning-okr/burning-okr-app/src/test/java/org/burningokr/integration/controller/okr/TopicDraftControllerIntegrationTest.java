package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.burningokr.controller.okr.TopicDraftController;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteTopicDraftDto;
import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.dto.okrUnit.OkrChildUnitDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.dto.okrUnit.OkrUnitDto;
import org.burningokr.dto.okrUnit.UnitType;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.topicDraft.ConvertTopicDraftToTeamService;
import org.burningokr.service.topicDraft.OkrTopicDraftService;
import org.burningokr.utils.Base64Strings;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {TopicDraftController.class})
class TopicDraftControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext applicationContext;

  @MockBean
  private OkrTopicDraftService okrTopicDraftService;
  @MockBean
  private DataMapper<OkrTopicDraft, OkrTopicDraftDto> okrTopicDraftMapper;
  @MockBean
  private DataMapper<OkrDepartment, OkrDepartmentDto> okrDepartmentMapper;
  @MockBean
  private DataMapper<NoteTopicDraft, NoteTopicDraftDto> noteTopicDraftMapper;
  @MockBean
  private ConvertTopicDraftToTeamService convertTopicDraftToTeamService;
  @MockBean
  private NoteRepository noteRepository;

  private OkrTopicDraftDto okrTopicDraftDto;
  private NoteTopicDraftDto noteTopicDraftDto;


  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.applicationContext)
        .build();

    okrTopicDraftDto = new OkrTopicDraftDto(
        OkrTopicDescriptionDto.builder()
            .id(500L)
            .name("the best okr description")
            .initiatorId(UUID.randomUUID())
            .stakeholders(new ArrayList<UUID>())
            .description("description")
            .contributesTo("Team")
            .delimitation("delimitation")
            .beginning("Hello World")
            .dependencies("dependecies")
            .resources("unlimited resources")
            .handoverPlan("handover plan")
            .startTeam(new ArrayList<UUID>())
            .build()
    );
    okrTopicDraftDto.setOkrParentUnitId(okrTopicDraftDto.getId());
    okrTopicDraftDto.setInitiator(new User());
    okrTopicDraftDto.setCurrentStatus(1);

    noteTopicDraftDto = new NoteTopicDraftDto(NoteDto.builder()
        .noteId(250L)
        .noteBody("this is a note")
        .userId(UUID.randomUUID())
        .date(LocalDateTime.now())
        .build()
    );
    noteTopicDraftDto.setParentTopicDraftId(noteTopicDraftDto.getNoteId());
  }

  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = applicationContext.getServletContext();

    assertNotNull(servletContext);
    assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(applicationContext.getBean(TopicDraftController.class));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus200_whenValidDtoIsGiven() throws Exception {
    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenNameSizeIsTooShort() throws Exception {
    okrTopicDraftDto.setName("");

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenNameSizeIsTooLong() throws Exception {
    okrTopicDraftDto.setName(StringUtils.repeat("a", 256));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenNameSizeIsTooLong() throws Exception {
    okrTopicDraftDto.setName(StringUtils.repeat("a", 256));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Name should not be longer than 255 characters."));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenStartTeamIsNullField() throws Exception {
    okrTopicDraftDto.setStartTeam(null);

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenStakeholdersIsNullField() throws Exception {
    okrTopicDraftDto.setStakeholders(null);

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenDelimitationIsTooShort() throws Exception {
    okrTopicDraftDto.setDelimitation("");

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenDelimitationIsTooLong() throws Exception {
    okrTopicDraftDto.setDelimitation(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenDelimitationIsTooLong() throws Exception {
    okrTopicDraftDto.setDelimitation(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Delimitation should not be longer than 1024 characters."));

  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenDescriptionIsTooShort() throws Exception {
    okrTopicDraftDto.setDescription("");

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenDescriptionIsTooLong() throws Exception {
    okrTopicDraftDto.setDescription(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenDescriptionIsTooLong() throws Exception {
    okrTopicDraftDto.setDescription(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Description should not be longer than 1024 characters."));

  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenContributesToIsTooShort() throws Exception {
    okrTopicDraftDto.setContributesTo("");

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenContributesToIsTooLong() throws Exception {
    okrTopicDraftDto.setContributesTo(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenContributesToIsTooLong() throws Exception {
    okrTopicDraftDto.setContributesTo(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Contributes To should not be longer than 1024 characters."));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenResourcesIsTooShort() throws Exception {
    okrTopicDraftDto.setResources("");

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenResourcesIsTooLong() throws Exception {
    okrTopicDraftDto.setResources(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenResourcesIsTooLong() throws Exception {
    okrTopicDraftDto.setResources(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Resources should not be longer than 1024 characters."));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenHandoverPlanIsTooShort() throws Exception {
    okrTopicDraftDto.setHandoverPlan("");

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenHandoverPlanIsTooLong() throws Exception {
    okrTopicDraftDto.setHandoverPlan(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenHandoverPlanIsTooLong() throws Exception {
    okrTopicDraftDto.setHandoverPlan(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Handover Over Plan should not be longer than 1024 characters."));
  }

  @Test
  void addNoteToTopicDraft_shouldReturnStatus200_whenNoteTopicDraftDtoIsValid() throws Exception {
    MvcResult result = this.mockMvc.perform(
        post("/api/topicDrafts/{topicDraftId}/notes", okrTopicDraftDto.getId())
            .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    ).andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void addNoteToTopicDraft_shouldReturnStatus400_whenNoteBodyIsTooShort() throws Exception {
    noteTopicDraftDto.setNoteBody("");

    MvcResult result = this.mockMvc.perform(
        post("/api/topicDrafts/{topicDraftId}/notes", okrTopicDraftDto.getId())
            .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    ).andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }


  @Test
  void addNoteToTopicDraft_shouldReturnStatus400_whenNoteBodyIsTooLong() throws Exception {
    noteTopicDraftDto.setNoteBody(StringUtils.repeat("a", 1026));

    MvcResult result = this.mockMvc.perform(
        post("/api/topicDrafts/{topicDraftId}/notes", okrTopicDraftDto.getId())
            .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON)
    ).andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addNoteToTopicDraft_shouldReturnErrorMessage_whenNoteBodyIsTooLong() throws Exception {
    noteTopicDraftDto.setNoteBody(StringUtils.repeat("a", 1026));

    MvcResult result = this.mockMvc.perform(
            post("/api/topicDrafts/{topicDraftId}/notes", okrTopicDraftDto.getId())
                .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andReturn();

    assertNotNull(result);
    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "The note text may not be longer than 1023 characters."));
  }

}
