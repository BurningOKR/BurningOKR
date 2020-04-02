package org.burningokr.dto.okr;

import java.time.LocalDateTime;
import java.util.UUID;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteDto {

  private Long noteId;

  private Long parentKeyResultId;

  private UUID userId;

  @NotNull
  @Size(max = 1023, message = "The note text may not be longer than 1023 characters.")
  private String noteBody;

  private LocalDateTime date;
}
