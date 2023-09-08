package org.burningokr.dto.okr;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NoteDto {

  private Long noteId;

  private UUID userId;

  @NotNull
  @Size(max = 1023, message = "The note text may not be longer than 1023 characters.")
  private String noteBody;

  private LocalDateTime date;

  public NoteDto(NoteDto noteDto) {
    this.noteId = noteDto.getNoteId();
    this.userId = noteDto.getUserId();
    this.noteBody = noteDto.getNoteBody();
    this.date = noteDto.getDate();
  }
}
