package org.burningokr.dto.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.dto.okrUnit.OkrUnitDto;
import org.springframework.lang.Nullable;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class StructureDto extends OkrUnitDto {

  @Nullable
  private Collection<StructureDto> substructure;
}
