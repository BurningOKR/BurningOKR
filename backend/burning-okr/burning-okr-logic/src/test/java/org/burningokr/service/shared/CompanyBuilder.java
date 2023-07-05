package org.burningokr.service.shared;

import org.burningokr.model.okrUnits.OkrCompany;

public class CompanyBuilder {
  OkrCompany currentCompany;

  private CompanyBuilder()
  {
    this.currentCompany = new OkrCompany();
  }

  public static CompanyBuilder Create()
  {
    return new CompanyBuilder();
  }

  public OkrCompany Build() {
    return currentCompany;
  }
}
