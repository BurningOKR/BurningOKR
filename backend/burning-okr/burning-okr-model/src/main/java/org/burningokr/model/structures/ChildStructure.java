package org.burningokr.model.structures;

public interface ChildStructure {
  void setParentStructure(Structure parentStructure);

  Structure getParentStructure();
}
