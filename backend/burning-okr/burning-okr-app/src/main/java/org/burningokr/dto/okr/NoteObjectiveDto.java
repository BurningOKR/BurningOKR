package org.burningokr.dto.okr;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class NoteObjectiveDto extends NoteDto {

  private Long parentObjectiveId;

  public NoteObjectiveDto(NoteDtoBuilder<?, ?> builder) {
    super(builder);
  }
}
