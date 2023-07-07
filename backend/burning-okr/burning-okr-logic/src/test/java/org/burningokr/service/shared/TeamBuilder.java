package org.burningokr.service.shared;

import org.burningokr.model.okr.Objective;
import org.burningokr.model.okrUnits.OkrBranch;
import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrDepartment;

import java.util.Collection;
import java.util.List;

public class TeamBuilder {
  OkrChildUnit currentTeam;

  public TeamBuilder(OkrChildUnit input)
  {
    this.currentTeam = input;
  }

  public static TeamBuilder CreateDepartment()
  {
    return new TeamBuilder(new OkrDepartment());
  }

  public static TeamBuilder CreateDepartment(long id)
  {
    OkrChildUnit department = new OkrDepartment();
    department.setId(id);
    return new TeamBuilder(department);
  }

  public static TeamBuilder CreateBranch()
  {
    return new TeamBuilder(new OkrBranch());
  }

  public TeamBuilder AddObjective(Objective objectiveToAdd)
  {
    Collection<Objective> objectives = currentTeam.getObjectives();

    if(objectives == null)
    {
      currentTeam.setObjectives(List.of(objectiveToAdd));
    }
    else
    {
      objectives.add(objectiveToAdd);
    }

    return this;
  }

  public OkrChildUnit Build()
  {
    return currentTeam;
  }
}
