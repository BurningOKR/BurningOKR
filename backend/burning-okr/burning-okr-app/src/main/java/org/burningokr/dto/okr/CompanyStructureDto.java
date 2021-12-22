package org.burningokr.dto.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.dto.okrUnit.OkrChildUnitDto;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.dto.okrUnit.OkrUnitSchemaDto;

import java.util.Collection;

@EqualsAndHashCode(callSuper = true)
@Data
public class CompanyStructureDto extends OkrCompanyDto {

    private Collection<OkrUnitSchemaDto> unitSchema;

}
