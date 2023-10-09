package org.burningokr.dto.okr;

import jakarta.persistence.Inheritance;
import lombok.*;
import lombok.experimental.SuperBuilder;


@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NoteObjectiveDto extends NoteDto {

  private Long parentObjectiveId;

}


