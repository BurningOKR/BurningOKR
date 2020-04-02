package org.burningokr.model.structures;

public interface ChildStructure {
  void setParentStructure(CompanyStructure parentStructure);

  CompanyStructure getParentStructure();
}
