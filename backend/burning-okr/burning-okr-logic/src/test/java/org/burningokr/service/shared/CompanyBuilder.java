package org.burningokr.service.shared;

import org.burningokr.model.okrUnits.OkrChildUnit;
import org.burningokr.model.okrUnits.OkrCompany;

import java.util.Collection;
import java.util.List;

public class CompanyBuilder {
  OkrCompany currentCompany;

  private CompanyBuilder()
  {
    this.currentCompany = new OkrCompany();
  }

  public static CompanyBuilder Create() {
    return new CompanyBuilder();
  }

  public CompanyBuilder AddChildUnits(OkrChildUnit childUnitToAdd)
  {
    Collection<OkrChildUnit> childUnits = currentCompany.getOkrChildUnits();

    if(childUnits == null)
    {
      currentCompany.setOkrChildUnits(List.of(childUnitToAdd));
    }
    else
    {
      childUnits.add(childUnitToAdd);
    }

    return this;
  }

  public OkrCompany Build() {
    return currentCompany;
  }
}
