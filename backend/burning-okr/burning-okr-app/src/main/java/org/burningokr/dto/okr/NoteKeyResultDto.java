package org.burningokr.dto.okr;

import lombok.*;

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
