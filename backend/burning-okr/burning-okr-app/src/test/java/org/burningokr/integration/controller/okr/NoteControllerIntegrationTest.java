package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okr.NoteController;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.mapper.okr.NoteMapper;
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
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
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


  @Test
  void getNoteById() throws Exception {
    MvcResult result = this.mockMvc.perform(get("/api/notes/{noteId}", 123))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void updateNoteById() throws Exception {
    final UUID userId = UUID.randomUUID();
    final LocalDateTime now = LocalDateTime.now();

    MvcResult result = this.mockMvc.perform(put("/api/notes/{noteId}", 123)
            .content(new ObjectMapper().writeValueAsString(new NoteDto(404L, userId, "body title for integration test", now)))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isAccepted())
        .andExpect(jsonPath("$.userId").value(userId))
        .andExpect(jsonPath("$.noteBody").value("body title for integration test"))
        .andExpect(jsonPath("$.date").value(now))
        .andReturn();

    Assertions.assertNotNull(userId);
    Assertions.assertNotNull(now);
    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void deleteNoteById() throws Exception {
    MvcResult result = this.mockMvc.perform(delete("/api/notes/{noteId}", 123))
        .andDo(print())
        .andExpect((ResultMatcher) content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }
}
