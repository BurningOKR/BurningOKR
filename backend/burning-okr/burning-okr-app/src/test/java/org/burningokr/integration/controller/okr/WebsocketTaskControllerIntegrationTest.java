package org.burningokr.integration.controller.okr;

import org.burningokr.controller.okr.WebsocketTaskController;
import org.burningokr.mapper.okr.TaskMapper;
import org.burningokr.service.okr.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest
@WebAppConfiguration
@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = {WebsocketTaskController.class})
class WebsocketTaskControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext applicationContext;

  @MockBean
  private SimpMessagingTemplate simpMessagingTemplate;
  @MockBean
  private TaskService taskService;
  @MockBean
  private TaskMapper taskMapper;

  @BeforeEach
  void setUp() {
    this.mockMvc = MockMvcBuilders.webAppContextSetup(this.applicationContext).build();
  }

  @Test
  void addTask() throws Exception {
//    MvcResult result = this.mockMvc.perform()
  }

  @Test
  void updateTask() {
  }

  @Test
  void deleteTask() {
  }
}
