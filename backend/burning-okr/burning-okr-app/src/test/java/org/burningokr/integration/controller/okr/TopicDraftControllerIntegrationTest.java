package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
import org.burningokr.controller.okr.TopicDraftController;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteTopicDraftDto;
import org.burningokr.dto.okr.OkrTopicDescriptionDto;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.model.users.User;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.topicDraft.ConvertTopicDraftToTeamService;
import org.burningokr.service.topicDraft.OkrTopicDraftService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
  void updateTopicResultById_shouldReturnStatus200_whenValidOkrTopicDraftDtoIsGiven() throws Exception {
    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoNameIsEmpty() throws Exception {
    okrTopicDraftDto.setName("");

    this.mockMvc.perform(
          put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
            .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenOkrTopicDraftDtoNameIsEmpty() throws Exception {
    okrTopicDraftDto.setName("");

    MvcResult result =
      this.mockMvc.perform(
          put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
            .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
            .contentType(MediaType.APPLICATION_JSON)
        )
        .andReturn();

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
      "Name should not be empty or longer than 255 characters."));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoNameIsTooLong() throws Exception {
    okrTopicDraftDto.setName(StringUtils.repeat("a", 256));

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenOkrTopicDraftDtoNameIsTooLong() throws Exception {
    okrTopicDraftDto.setName(StringUtils.repeat("a", 256));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Name should not be empty or longer than 255 characters."));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoStartTeamIsNull() throws Exception {
    okrTopicDraftDto.setStartTeam(null);

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoStakeholdersIsNull() throws Exception {
    okrTopicDraftDto.setStakeholders(null);

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoDelimitationIsTooShort() throws Exception {
    okrTopicDraftDto.setDelimitation("");

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoDelimitationIsTooLong() throws Exception {
    okrTopicDraftDto.setDelimitation(StringUtils.repeat("a", 1025));

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenOkrTopicDraftDtoDelimitationIsTooLong() throws Exception {
    okrTopicDraftDto.setDelimitation(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Delimitation should not be empty or longer than 1023 characters."));

  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoDescriptionIsTooShort() throws Exception {
    okrTopicDraftDto.setDescription("");

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoDescriptionIsTooLong() throws Exception {
    okrTopicDraftDto.setDescription(StringUtils.repeat("a", 1025));

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenOkrTopicDraftDtoDescriptionIsTooLong() throws Exception {
    okrTopicDraftDto.setDescription(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Description should not be empty or longer than 1023 characters."));

  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoContributesToIsTooShort() throws Exception {
    okrTopicDraftDto.setContributesTo("");

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoContributesToIsTooLong() throws Exception {
    okrTopicDraftDto.setContributesTo(StringUtils.repeat("a", 1025));

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenOkrTopicDraftDtoContributesToIsTooLong() throws Exception {
    okrTopicDraftDto.setContributesTo(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Contributes To should not be empty or longer than 1023 characters."));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoResourcesIsTooShort() throws Exception {
    okrTopicDraftDto.setResources("");

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoResourcesIsTooLong() throws Exception {
    okrTopicDraftDto.setResources(StringUtils.repeat("a", 1025));

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenOkrTopicDraftDtoResourcesIsTooLong() throws Exception {
    okrTopicDraftDto.setResources(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
          .andExpect(status().isBadRequest())
            .andReturn();

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Resources should not be empty or longer than 1023 characters."));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoHandoverPlanIsTooShort() throws Exception {
    okrTopicDraftDto.setHandoverPlan("");

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoHandoverPlanIsTooLong() throws Exception {
    okrTopicDraftDto.setHandoverPlan(StringUtils.repeat("a", 1025));

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }

  @Test
  void updateTopicResultById_shouldReturnErrorMessage_whenOkrTopicDraftDtoHandoverPlanIsTooLong() throws Exception {
    okrTopicDraftDto.setHandoverPlan(StringUtils.repeat("a", 1025));

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "Handover Over Plan should not be empty or longer than 1023 characters."));
  }

  @Test
  void addNoteToTopicDraft_shouldReturnStatus200_whenNoteTopicDraftDtoIsValid() throws Exception {
    this.mockMvc.perform(
        post("/api/topicDrafts/{topicDraftId}/notes", okrTopicDraftDto.getId())
            .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isOk());
  }

  @Test
  void addNoteToTopicDraft_shouldReturnStatus400_whenNoteTopicDraftDtoNoteBodyIsNull() throws Exception {
    noteTopicDraftDto.setNoteBody(null);

    this.mockMvc.perform(
        post("/api/topicDrafts/{topicDraftId}/notes", okrTopicDraftDto.getId())
            .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest());
  }


  @Test
  void addNoteToTopicDraft_shouldReturnStatus400_whenNoteTopicDraftDtoNoteBodyIsTooLong() throws Exception {
    noteTopicDraftDto.setNoteBody(StringUtils.repeat("a", 1026));

    this.mockMvc.perform(
        post("/api/topicDrafts/{topicDraftId}/notes", okrTopicDraftDto.getId())
            .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
            .contentType(MediaType.APPLICATION_JSON)
    ).andExpect(status().isBadRequest());
  }

  @Test
  void addNoteToTopicDraft_shouldReturnErrorMessage_whenNoteTopicDraftDtoNoteBodyIsTooLong() throws Exception {
    noteTopicDraftDto.setNoteBody(StringUtils.repeat("a", 1026));

    MvcResult result = this.mockMvc.perform(
            post("/api/topicDrafts/{topicDraftId}/notes", okrTopicDraftDto.getId())
                .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
                .contentType(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isBadRequest())
        .andReturn();

    assertTrue(StringUtils.contains(Objects.requireNonNull(result.getResolvedException()).getMessage(),
        "The note text may not be empty or longer than 1023 characters."));
  }

  @Test
  void updateTopicResultById_shouldReturnStatus400_whenOkrTopicDraftDtoCurrentStatusIsLessThanZero() throws Exception {
    okrTopicDraftDto.setCurrentStatus(-1);

    this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", okrTopicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(okrTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest());
  }
}
