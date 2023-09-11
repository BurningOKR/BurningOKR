package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okr.KeyResultSequenceController;
import org.burningokr.controller.okr.TopicDraftController;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.dto.okr.NoteTopicDraftDto;
import org.burningokr.dto.okr.OkrTopicDraftDto;
import org.burningokr.dto.okrUnit.OkrDepartmentDto;
import org.burningokr.mapper.interfaces.DataMapper;
import org.burningokr.model.okr.Note;
import org.burningokr.model.okr.NoteTopicDraft;
import org.burningokr.model.okr.okrTopicDraft.OkrTopicDraft;
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
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockServletContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import javax.print.attribute.standard.Media;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    Assertions.assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    Assertions.assertNotNull(applicationContext.getBean(KeyResultSequenceController.class));
  }

  @Test
  void getAllCompanies_shouldReturnAllAvailableCompanies() throws Exception {
    MvcResult result = this.mockMvc.perform(get("/api/topicDrafts"))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void getNotesForTopicDraft() throws Exception {
    NoteTopicDraft noteTopicDraft = new NoteTopicDraft(createNoteForTesting());

    MvcResult result = this.mockMvc.perform(get("/api/topicDrafts/{topicDraftId}/notes", noteTopicDraft.getId()))
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(noteTopicDraft);
    Assertions.assertEquals(510L, (long) noteTopicDraft.getId());
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void updateTopicResultById() throws Exception {


  }

  @Test
  void updateTopicResultStatusById() {
  }

  @Test
  void addNoteToTopicDraft() throws Exception {
    NoteTopicDraft draft = new NoteTopicDraft(createNoteForTesting());


    MvcResult result = this.mockMvc.perform(post("/api/topicDrafts/{topicDraftId}/notes", draft.getId())
            .content(new ObjectMapper().writeValueAsString(new NoteKeyResultDto(noteTopicDraftMapper.mapEntityToDto(draft))))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.noteBody").value("created a note for topic draft integration controller test"))
        .andExpect(jsonPath("$.nodeId").value(draft.getId()))
        .andReturn();

    Assertions.assertNotNull(draft);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void createOkrTopicDraft() {
  }

  @Test
  void deleteTopicDraftById() {
  }

  @Test
  void convertTopicDraftToTeam() {
  }

  private Note createNoteForTesting() {
    return noteRepository.save(new Note(510L, UUID.randomUUID(), "Created a new note for integration testing", LocalDateTime.now()));
  }
}
