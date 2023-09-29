package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okr.TopicDraftController;
import org.burningokr.dto.okr.NoteTopicDraftDto;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
import org.burningokr.model.okrUnits.OkrCompany;
import org.burningokr.model.okrUnits.OkrDepartment;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.topicDraft.ConvertTopicDraftToTeamService;
import org.burningokr.service.topicDraft.OkrTopicDraftService;
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


  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.applicationContext)
        .build();
  }

  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = applicationContext.getServletContext();

    assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    assertNotNull(applicationContext.getBean(TopicDraftController.class));
  }

  @Test
  void updateTopicResultById_shouldCheckIfDtoIsValid() throws Exception {
    OkrTopicDraftDto topicDraftDto = createValidTopicDraft(createCompany());

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", topicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(topicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();
    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultById_shouldCheckIfDtoIsNotValid() throws Exception {
    OkrTopicDraftDto topicDraftDto = createNonValidTopicDraft(createCompany());

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/{topicDraftId}", topicDraftDto.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(topicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();
    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultStatusById_shouldCheckIfDtoIsValid() throws Exception {
    OkrTopicDraftDto topicDraftDto = createValidTopicDraft(createCompany());

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/status/{topicDraftId}", topicDraftDto.getId())
                    .content(new ObjectMapper().writeValueAsString(topicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateTopicResultStatusById_shouldCheckIfDtoIsNonValid() throws Exception {
    OkrTopicDraftDto topicDraftDto = createNonValidTopicDraft(createCompany());

    MvcResult result =
        this.mockMvc.perform(
                put("/api/topicDrafts/status/{topicDraftId}", topicDraftDto.getId())
                    .content(new ObjectMapper().writeValueAsString(topicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addNoteToTopicDraft_shouldCheckIfValidDto() throws Exception {
    OkrTopicDraftDto topic = createValidTopicDraft(createCompany());
    NoteTopicDraftDto noteTopicDraftDto = createValidNoteDraft(topic);

    MvcResult result =
        this.mockMvc.perform(
                post("/api/topicDrafts/{topicDraftId}/notes", topic.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void addNoteToTopicDraft_shouldCheckIfNonValidDto() throws Exception {
    OkrTopicDraftDto topic = createValidTopicDraft(createCompany());
    NoteTopicDraftDto noteTopicDraftDto = createNonValidNoteDraft(topic);

    MvcResult result =
        this.mockMvc.perform(
                post("/api/topicDrafts/{topicDraftId}/notes", topic.getId())
                    .content(new ObjectMapper().findAndRegisterModules().writeValueAsString(noteTopicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void createOkrTopicDraft_shouldCheckIfValidDto() throws Exception {
    OkrTopicDraftDto topicDraftDto = createValidTopicDraft(createCompany());

    MvcResult result =
        this.mockMvc.perform(
                post("/api/topicDrafts/create")
                    .content(new ObjectMapper().writeValueAsString(topicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isOk())
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void createOkrTopicDraft_shouldCheckIfNonValidDto() throws Exception {
    OkrTopicDraftDto topicDraftDto = createNonValidTopicDraft(createCompany());

    MvcResult result =
        this.mockMvc.perform(
                post("/api/topicDrafts/create")
                    .content(new ObjectMapper().writeValueAsString(topicDraftDto))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  private static OkrCompany createCompany() {
    OkrCompany company = new OkrCompany();
    company.setId(100L);
    return company;
  }

  private static OkrTopicDraftDto createValidTopicDraft(OkrCompany company) {
    OkrTopicDraftDto topicDraftDto = new OkrTopicDraftDto();
    topicDraftDto.setId(120L);
    topicDraftDto.setOkrParentUnitId(company.getId());
    topicDraftDto.setBeginning("29-09-2023");
    topicDraftDto.setCurrentStatus(1);
    topicDraftDto.setDependencies("dependencies");
    topicDraftDto.setContributesTo("contributes to");
    topicDraftDto.setHandoverPlan("handover plan");
    topicDraftDto.setDescription("description");
    topicDraftDto.setInitiatorId(UUID.randomUUID());
    topicDraftDto.setResources("resources");
    topicDraftDto.setName("name");
    topicDraftDto.setStakeholders(new ArrayList<>());
    topicDraftDto.setStartTeam(new ArrayList<>());

    return topicDraftDto;
  }

  private static OkrTopicDraftDto createNonValidTopicDraft(OkrCompany company) {
    OkrTopicDraftDto topicDraftDto = new OkrTopicDraftDto();
    topicDraftDto.setId(120L);
    topicDraftDto.setOkrParentUnitId(company.getId());
    topicDraftDto.setBeginning("29-09-2023");
    topicDraftDto.setCurrentStatus(-50);
    topicDraftDto.setDependencies("");
    topicDraftDto.setContributesTo("");
    topicDraftDto.setHandoverPlan("");
    topicDraftDto.setDescription("");
    topicDraftDto.setInitiatorId(UUID.randomUUID());
    topicDraftDto.setResources("");
    topicDraftDto.setName("");
    topicDraftDto.setStakeholders(new ArrayList<>());
    topicDraftDto.setStartTeam(new ArrayList<>());
    return topicDraftDto;
  }

  private static NoteTopicDraftDto createValidNoteDraft(OkrTopicDraftDto topic) {
    NoteTopicDraftDto noteTopicDraftDto = new NoteTopicDraftDto();
    noteTopicDraftDto.setNoteId(140L);
    noteTopicDraftDto.setParentTopicDraftId(topic.getId());
    noteTopicDraftDto.setNoteBody("a");
    noteTopicDraftDto.setDate(LocalDateTime.now());
    noteTopicDraftDto.setUserId(UUID.randomUUID());
    return noteTopicDraftDto;
  }

  private static NoteTopicDraftDto createNonValidNoteDraft(OkrTopicDraftDto topic) {
    NoteTopicDraftDto noteTopicDraftDto = new NoteTopicDraftDto();
    noteTopicDraftDto.setNoteId(140L);
    noteTopicDraftDto.setParentTopicDraftId(topic.getId());
    noteTopicDraftDto.setNoteBody("");
    noteTopicDraftDto.setDate(LocalDateTime.now());
    noteTopicDraftDto.setUserId(UUID.randomUUID());
    return noteTopicDraftDto;
  }
}
