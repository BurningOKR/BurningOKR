package org.burningokr.dto.okr;

import lombok.*;
import lombok.experimental.SuperBuilder;
import net.bytebuddy.implementation.bind.annotation.Super;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class NoteKeyResultDto extends NoteDto {

  private Long parentKeyResultId;

  public NoteKeyResultDto() {
    super();
  }

  public NoteKeyResultDto(NoteDto noteDto) {
    super(noteDto);
  }
}
