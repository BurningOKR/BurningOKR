package org.burningokr.dto.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.dto.okrUnit.OkrCompanyDto;
import org.burningokr.model.okrUnits.OkrChildUnit;
import java.util.Collection;

@Data
public class CompanyStructureDto {

    private Collection<OkrChildUnit> ChildUnits;

    private String name;

    private long id;

}
