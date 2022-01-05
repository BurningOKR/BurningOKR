package org.burningokr.dto.okr;

import java.util.Collection;
import javax.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.dto.okrUnit.OkrUnitDto;

@EqualsAndHashCode(callSuper = true)
@Data
public class StructureDto extends OkrUnitDto {

  @Nullable private Collection<StructureDto> substructure;
}
