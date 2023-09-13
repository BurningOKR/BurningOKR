package org.burningokr.service.okr;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.okr.DefaultTaskBoardState;
import org.burningokr.model.okr.TaskBoard;
import org.burningokr.model.okr.TaskState;
import org.burningokr.repositories.okr.DefaultTaskStateRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
@RequiredArgsConstructor
public class DefaultTaskStateService {
  private final DefaultTaskStateRepository defaultStatesRepository;

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
