package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.burningokr.controller.okr.KeyResultController;
import org.burningokr.dto.okr.KeyResultDto;
import org.burningokr.dto.okr.NoteDto;
import org.burningokr.dto.okr.NoteKeyResultDto;
import org.burningokr.mapper.okr.KeyResultMapper;
import org.burningokr.mapper.okr.NoteKeyResultMapper;
import org.burningokr.model.okr.Unit;
import org.burningokr.service.okr.KeyResultService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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


  @BeforeEach
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.context).build();
  }

  @Test
  public void getServletContext_shouldCheckIfContextIsLoaded() {
    ServletContext servlet = context.getServletContext();

    Assertions.assertNotNull(servlet);
    Assertions.assertTrue(servlet instanceof MockServletContext);
    Assertions.assertNotNull(context.getBean(KeyResultController.class));
  }

  @Test
  void getKeyResultById() throws Exception {
    MvcResult result = this.mockMvc
        .perform(get("/api/keyresults/{keyResultId}", 280))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());

  }

  @Test
  void getNotesOfKeyResult() throws Exception {
    //find all possible keyresults from a departement dynamically from database and save it's id as string


    MvcResult result = this.mockMvc
        .perform(get("/api/keyresults/{keyresultId}/notes", 280))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON))
        .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void updateKeyResultById() throws Exception {

    MvcResult result = this.mockMvc
        .perform(post("/api/keyresults/{keyresultId}/notes", 400)
            .content(new ObjectMapper().writeValueAsString(new KeyResultDto(400L, 380L, "simple title", "simple desc", 0, 1, 10, Unit.NUMBER, 1, new ArrayList<>(), new ArrayList<>())))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.title").value("simple title"))
        .andExpect(jsonPath("$.description").value("simple desc"))
        .andReturn();

    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, result.getResponse().getContentType());
  }

  @Test
  void addNoteToKeyResult() throws Exception {

    final UUID userId = UUID.randomUUID();
    final LocalDateTime now = LocalDateTime.now();

    MvcResult mvcResult =
        this.mockMvc.perform(
                post("/api/keyresults/{keyresultId}/notes", 400)
                    .content(new ObjectMapper().writeValueAsString(new NoteKeyResultDto(new NoteDto(1000L, userId, "integration testing", now))))
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(jsonPath("$.noteId").value("1000"))
            .andExpect(jsonPath("$.userId").value(userId))
            .andExpect(jsonPath("$.noteBody").value("integration testing"))
            .andExpect(jsonPath("$.date").value(now.toString()))
            .andReturn();

    Assertions.assertNotNull(mvcResult);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

  @Test
  void updateNoteKeyResult() throws Exception {
    final UUID userId = UUID.randomUUID();
    final LocalDateTime now = LocalDateTime.now();
    MvcResult mvcResult = this.mockMvc
        .perform(put("/api/keyresults/notes")
            .content(new ObjectMapper().writeValueAsString(new NoteKeyResultDto(new NoteDto(0L, userId, "update note to key result int_test", now))))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(jsonPath("$.userId").value(userId))
        .andExpect(jsonPath("$.noteBody").value("update note to key result int_test"))
        .andExpect(jsonPath("$.date").value(now.toString()))
        .andReturn();

    Assertions.assertNotNull(mvcResult);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

  @Test
  void deleteKeyResult() throws Exception {
    MvcResult mvcResult = this.mockMvc.perform(delete("/api/keyresults/{keyresultId}", 400))
        .andDo(print())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        .andReturn();

    Assertions.assertNotNull(mvcResult);
    Assertions.assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
  }

}
