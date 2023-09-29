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
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


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
  void updateNoteById_shouldCheckIfValidDto() throws Exception {
    NoteDto noteDto = new NoteDto();
    noteDto.setNoteId(110L);
    noteDto.setNoteBody("body");
    noteDto.setUserId(UUID.randomUUID());
    noteDto.setDate(LocalDateTime.now());

    MvcResult result = this.mockMvc.perform(put("/api/notes/{noteId}", noteDto.getNoteId())
            .content(
                new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteDto)
            )
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(noteDto);
    Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateNoteById_shouldCheckIfNonValidDto() throws Exception {
    NoteDto noteDto = new NoteDto();
    noteDto.setNoteId(110L);
    noteDto.setNoteBody("");
    noteDto.setUserId(UUID.randomUUID());
    noteDto.setDate(LocalDateTime.now());

    MvcResult result = this.mockMvc.perform(put("/api/notes/{noteId}", noteDto.getNoteId())
            .content(
                new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(noteDto)
            )
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(noteDto);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }
}