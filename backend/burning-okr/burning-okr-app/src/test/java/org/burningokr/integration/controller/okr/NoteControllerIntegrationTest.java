package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okr.NoteController;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.okr.NoteMapper;
import org.burningokr.model.okr.Note;
import org.burningokr.repositories.okr.NoteRepository;
import org.burningokr.service.okr.NoteService;
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
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {NoteController.class})
class NoteControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @MockBean
  private NoteService noteService;

  @MockBean
  private NoteMapper noteMapper;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.webApplicationContext)
        .build();
  }

  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    Assertions.assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    Assertions.assertNotNull(webApplicationContext.getBean(NoteController.class));
  }


  @Test // FIXME: content type is not set due to unknown reasons
  void getNoteById() throws Exception {
    MvcResult result = this.mockMvc.perform(MockMvcRequestBuilders.get("/api/notes/{noteId}", 351))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void updateNoteById() throws Exception {
    final UUID userId = UUID.randomUUID();
    final LocalDateTime now = LocalDateTime.now();
    final ObjectMapper mapper = JsonMapper.builder().findAndAddModules().build();

    NoteDto noteDto = new NoteDto(410L, userId, "simple note to test", now);


    MvcResult result = this.mockMvc.perform(put("/api/notes/{noteId}", 0)
            .content(mapper.writeValueAsString(noteDto))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.userId").value(userId))
        .andExpect(jsonPath("$.noteBody").value("simple note to test"))
        .andExpect(jsonPath("$.date").value(now))
        .andReturn();

    Assertions.assertNotNull(userId);
    Assertions.assertNotNull(now);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void deleteNoteById() throws Exception {
    MvcResult result = this.mockMvc.perform(delete("/api/notes/{noteId}", 353))
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }
}
