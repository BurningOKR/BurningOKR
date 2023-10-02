package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
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
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;


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

  private NoteDto validNoteDto;

  @BeforeEach
  void setup() {
    this.mockMvc = MockMvcBuilders
        .webAppContextSetup(this.webApplicationContext)
        .build();

    this.validNoteDto = NoteDto.builder()
        .noteId(42L)
        .userId(UUID.randomUUID())
        .noteBody("Hello World")
        .date(LocalDateTime.now())
        .build();
  }

  // check if context is build correctly
  @Test
  public void getServletContext_shouldCheckIfEverythingIsLoadedCorrect() {
    ServletContext servletContext = webApplicationContext.getServletContext();

    Assertions.assertNotNull(servletContext);
    Assertions.assertTrue(servletContext instanceof MockServletContext);
    Assertions.assertNotNull(webApplicationContext.getBean(NoteController.class));
  }

  // actual testing
  @Test
  void updateNoteById_shouldReturnStatus200_whenDtoIsValid() throws Exception {
    MvcResult result = this.mockMvc.perform(put("/api/notes/{noteId}", this.validNoteDto.getNoteId())
            .content(
                new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(this.validNoteDto)
            )
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(this.validNoteDto);
    Assertions.assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateNoteById_shouldReturnStatus400_whenDtoNoteBodyIsTooShort() throws Exception {
    NoteDto invalidDto = this.validNoteDto;
    invalidDto.setNoteBody("");

    MvcResult result = this.mockMvc.perform(put("/api/notes/{noteId}", invalidDto.getNoteId())
            .content(
                new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(invalidDto)
            )
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(invalidDto);
    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateNoteById_shouldReturnStatus400_whenDtoNoteBodyIsTooLong() throws Exception {
    NoteDto invalidDto = this.validNoteDto;
    invalidDto.setNoteBody(StringUtils.repeat("A", 1024));

    MvcResult result = this.mockMvc.perform(put("/api/notes/{noteId}", invalidDto.getNoteId())
            .content(
                new ObjectMapper()
                    .findAndRegisterModules()
                    .writeValueAsString(invalidDto)
            )
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON))
        .andReturn();

    Assertions.assertNotNull(result);
    Assertions.assertNotNull(invalidDto);
    String defaultMessage = result.getResolvedException().getMessage()
        .trim().split("default message")[2];
    String test =
        defaultMessage.trim().substring(0, defaultMessage.length() - 2);
    System.out.println(test);
//    System.out.println(result.getResolvedException().getMessage().split("default message")[2]);
    //List<String> errors = result.getHandler()..getFieldErrors()
    //    .stream().map(this::constructErrorMessage).collect(Collectors.toList());
    Assertions.assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }
}
