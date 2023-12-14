package org.burningokr.dto.okr;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class NoteDto {

  private Long noteId;

  private UUID userId;

  @NotNull
  @Size(
      max = 1023,
      message = "The note text may not be longer than 1023 characters."
  )
  private String noteBody;

  private LocalDateTime date;


  public NoteDto(NoteDtoBuilder<?,?> b) {
    this.noteId = b.noteId;
    this.userId = b.userId;
    this.noteBody = b.noteBody;
    this.date = b.date;
  }

  public NoteDto(NoteDto noteDto) {
    this.noteId = noteDto.getNoteId();
    this.userId = noteDto.getUserId();
    this.noteBody = noteDto.getNoteBody();
    this.date = noteDto.getDate();
  }
}


