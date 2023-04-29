package org.burningokr.service.okr;

import org.burningokr.model.okr.DefaultTaskBoardState;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.okr.DefaultTaskStateRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class DefaultTaskStateService {
  private final Logger logger = LoggerFactory.getLogger(TaskService.class);
  private final DefaultTaskStateRepository defaultStatesRepository;

  @Autowired
  public DefaultTaskStateService(DefaultTaskStateRepository defaultStatesRepository) {
    this.defaultStatesRepository = defaultStatesRepository;
  }

  public Collection<DefaultTaskBoardState> findDefaultTaskStates() {
    ArrayList<DefaultTaskBoardState> defaultStates = new ArrayList<>();
    Iterable<DefaultTaskBoardState> stateIterable = defaultStatesRepository.findAll();
    for (DefaultTaskBoardState state : stateIterable) {
      defaultStates.add(state);
    }
    return defaultStates;
  }

  public Collection<TaskState> getDefaultTaskStatesForNewTaskBoard(TaskBoard parentTaskBoard) {
    ArrayList<TaskState> states = new ArrayList<>();
    Iterable<DefaultTaskBoardState> stateIterable = defaultStatesRepository.findAll();
    for (DefaultTaskBoardState defaultState : stateIterable) {
      TaskState state = new TaskState();
      state.setTitle(defaultState.getTitle());
      states.add(state);
      state.setParentTaskBoard(parentTaskBoard);
    }
    return states;
  }
}
