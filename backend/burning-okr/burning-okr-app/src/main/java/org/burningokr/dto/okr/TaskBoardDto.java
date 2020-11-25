package org.burningokr.dto.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.burningokr.model.okr.Task;
import org.burningokr.model.okrUnits.OkrUnit;

import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class TaskBoardDto {
    private Long id;
    private Long parentOkrUnitId;

    private Collection<Long> taskIds = new ArrayList<>();
}
