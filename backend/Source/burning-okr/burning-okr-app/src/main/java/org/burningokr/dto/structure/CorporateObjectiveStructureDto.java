package org.burningokr.dto.structure;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CorporateObjectiveStructureDto {

  @NotNull private Long id;

  @NotNull private String name;

  @NotNull private String label;

  @NotNull private Long parentStructureId;

  private Collection<Long> corporateObjectiveStructureIds = new ArrayList<>();

  private Collection<Long> departmentIds = new ArrayList<>();

  private Collection<Long> objectiveIds = new ArrayList<>();
}
