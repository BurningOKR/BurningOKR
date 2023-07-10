package org.burningokr.service.shared;

import org.burningokr.model.okr.KeyResult;
import org.burningokr.model.okr.Objective;

import java.util.Collection;
import java.util.List;

public class ObjectiveBuilder {
  Objective currentObjective;

  private ObjectiveBuilder()
  {
    this.currentObjective = new Objective();
  }

  public static ObjectiveBuilder Create()
  {
    return new ObjectiveBuilder();
  }

  public ObjectiveBuilder AddKeyResult(KeyResult keyResult)
  {
    Collection<KeyResult> keyResults = currentObjective.getKeyResults();

    if(keyResults == null)
    {
      currentObjective.setKeyResults(List.of(keyResult));
    }
    else
    {
      keyResults.add(keyResult);
    }

    return this;
  }

  public Objective Build()
  {
    return currentObjective;
  }
}
