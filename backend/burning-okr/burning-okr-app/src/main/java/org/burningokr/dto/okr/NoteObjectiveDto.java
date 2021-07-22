package org.burningokr.dto.okr;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteObjectiveDto extends NoteDto {

    private Long parentObjectiveId;
}
