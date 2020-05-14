package org.burningokr.service.initialisation;

import lombok.RequiredArgsConstructor;
import org.burningokr.model.initialisation.InitStateName;
import org.burningokr.service.exceptions.InvalidInitStateException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public abstract class InitOrderService {

  private final InitStateName[] initStateNames;

  /**
   * ∏ Gets the Next Init State.
   *
   * @param current an {@link InitStateName}
   * @return the next {@link InitStateName}
   * @throws InvalidInitStateException when there is no InitState after the given InitState.
   */
  public InitStateName getNextInitState(InitStateName current) throws InvalidInitStateException {
    for (int i = 0; i < initStateNames.length; i++) {
      if (initStateNames[i] == current && i < initStateNames.length - 1) {
        return initStateNames[i + 1];
      }
    }
    throw new InvalidInitStateException("There is no InitState after the given InitState.");
  }

  /**
   * Gets the initial Init State, with whom the application should start.
   *
   * @return the initial {@link InitStateName}
   */
  public InitStateName getInitialInitState() {
    return this.initStateNames[0];
  }
}
