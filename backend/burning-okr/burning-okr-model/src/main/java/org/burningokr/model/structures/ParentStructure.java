package org.burningokr.model.structures;

import java.util.Collection;

public interface ParentStructure {
  Collection<SubStructure> getSubStructures();

  void setSubStructures(Collection<SubStructure> departments);
}
