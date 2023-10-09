package org.burningokr.integration.controller.okr;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletContext;
import org.apache.commons.lang3.StringUtils;
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

  private NoteKeyResultDto noteKeyResultDto;
  private KeyResultDto keyResultDto;

  @BeforeEach
  void setUp() {
    this.mockMvc =
        MockMvcBuilders.webAppContextSetup(this.context).build();

    keyResultDto = KeyResultDto.builder()
        .id(120L)
        .parentObjectiveId(110L)
        .title("title")
        .description("description")
        .startValue(1L)
        .currentValue(1L)
        .targetValue(10L)
        .sequence(1)
        .unit(Unit.NUMBER)
        .noteIds(new ArrayList<>())
        .keyResultMilestoneDtos(new ArrayList<>())
        .build();

    noteKeyResultDto = new NoteKeyResultDto(NoteDto.builder()
        .noteId(122L)
        .noteBody("Hello Burning OKR")
        .userId(UUID.randomUUID())
        .date(LocalDateTime.now())
        .build());
    noteKeyResultDto.setParentKeyResultId(keyResultDto.getId());
  }

  @Test
  public void getServletContext_shouldCheckIfContextIsLoaded() {
    ServletContext servlet = context.getServletContext();

    assertNotNull(servlet);
    Assertions.assertTrue(servlet instanceof MockServletContext);
    assertNotNull(context.getBean(KeyResultController.class));
  }

  @Test
  void updateKeyResultById_shouldShouldStatus200_whenDtoIsValid() throws Exception {

    MvcResult result =
        this.mockMvc.perform(
            put("/api/keyresults/{keyResultId}", keyResultDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        ).andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.OK.value(), result.getResponse().getStatus());
  }

  @Test
  void updateKeyResultById_shouldReturnStatus400_whenParentObjectiveIsNull() throws Exception {
    keyResultDto.setParentObjectiveId(null);

    MvcResult result =
        this.mockMvc.perform(put("/api/keyresults/{keyResultId}", keyResultDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateKeyResultById_shouldReturnStatus400_whenTitleIsTooShort() throws Exception {
    keyResultDto.setTitle("");

    MvcResult result =
        this.mockMvc.perform(put("/api/keyresults/{keyResultId}", keyResultDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateKeyResultById_shouldReturnStatus400_whenTitleIsTooLong() throws Exception {
    keyResultDto.setTitle(StringUtils.repeat("A", 256));

    MvcResult result =
        this.mockMvc.perform(put("/api/keyresults/{keyResultId}", keyResultDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateKeyResultById_shouldReturnStatus400_whenDescriptionIsTooLong() throws Exception {
    keyResultDto.setDescription(StringUtils.repeat("A", 1024));

    MvcResult result =
        this.mockMvc.perform(put("/api/keyresults/{keyResultId}", keyResultDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateKeyResultById_shouldReturnStatus400_whenStartValueIsNegative() throws Exception {
    keyResultDto.setStartValue(-1L);

    MvcResult result =
        this.mockMvc.perform(put("/api/keyresults/{keyResultId}", keyResultDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateKeyResultById_shouldReturnStatus400_whenCurrentValueIsNegative() throws Exception {
    keyResultDto.setStartValue(-1L);

    MvcResult result =
        this.mockMvc.perform(put("/api/keyresults/{keyResultId}", keyResultDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void updateKeyResultById_shouldReturnStatus400_whenTargetValueIsNegative() throws Exception {
    keyResultDto.setStartValue(-1L);

    MvcResult result =
        this.mockMvc.perform(put("/api/keyresults/{keyResultId}", keyResultDto.getId())
                .content(new ObjectMapper().writeValueAsString(keyResultDto))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
            )
            .andExpect(status().isBadRequest())
            .andReturn();

    assertNotNull(result);
    assertNotNull(keyResultDto);
    assertEquals(HttpStatus.BAD_REQUEST.value(), result.getResponse().getStatus());
  }

  @Test
  void addNoteToKeyResult_shouldReturnStatus200_whenNoteKeyResultDtoIsValid() throws Exception {

    MvcResult mvcResult =
        this.mockMvc.perform(
                post("/api/keyresults/{keyresultId}/notes", keyResultDto.getId())
                    .content(new ObjectMapper()
                        .findAndRegisterModules()
                        .writeValueAsString(noteKeyResultDto)
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(mvcResult);
    assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  void addNoteToKeyResult_shouldReturnStatus400_whenNoteKeyResultDtoHasToShortNoteBody() throws Exception {
    noteKeyResultDto.setNoteBody("");

    MvcResult mvcResult =
        this.mockMvc.perform(
                post("/api/keyresults/{keyresultId}/notes", keyResultDto.getId())
                    .content(new ObjectMapper()
                        .findAndRegisterModules()
                        .writeValueAsString(noteKeyResultDto)
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(mvcResult);
    assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
  }

  @Test
  void addNoteToKeyResult_shouldReturnStatus400_whenNoteKeyResultDtoHasToLongNoteBody() throws Exception {
    noteKeyResultDto.setNoteBody(StringUtils.repeat("a", 1024));

    MvcResult mvcResult =
        this.mockMvc.perform(
                post("/api/keyresults/{keyresultId}/notes", keyResultDto.getId())
                    .content(new ObjectMapper()
                        .findAndRegisterModules()
                        .writeValueAsString(noteKeyResultDto)
                    )
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andReturn();

    assertNotNull(mvcResult);
    assertEquals(HttpStatus.BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
  }
}
