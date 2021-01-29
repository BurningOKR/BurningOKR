package org.burningokr.service.okr;

import org.burningokr.model.okr.DefaultTaskState;
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
    public DefaultTaskStateService(
            DefaultTaskStateRepository defaultStatesRepository
    ) {
        this.defaultStatesRepository = defaultStatesRepository;
    }

    public Collection<DefaultTaskState> findDefaultTaskStates() {
        ArrayList<DefaultTaskState> defaultStates = new ArrayList<>();
        Iterable<DefaultTaskState> stateIterable = defaultStatesRepository.findAll();
        for (DefaultTaskState state : stateIterable) {
            defaultStates.add(state);
        }
        return defaultStates;
    }
}
