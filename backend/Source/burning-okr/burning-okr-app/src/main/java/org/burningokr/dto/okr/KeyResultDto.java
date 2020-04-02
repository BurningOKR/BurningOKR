package org.burningokr.dto.okr;

import java.util.ArrayList;
import java.util.Collection;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import javax.validation.constraints.Size;
import lombok.Data;
import org.burningokr.model.okr.Unit;

@Data
public class KeyResultDto {

  private Long id;

  private Long parentObjectiveId;

  @NotNull
  @Size(max = 255, message = "The title of a key result may not be longer than 255 characters.")
  private String title;

  @NotNull
  @Size(
      max = 1023,
      message = "The description of a key result may not be longer than 1023 characters.")
  private String description;

  @PositiveOrZero private long startValue;

  @PositiveOrZero private long currentValue;

  @PositiveOrZero private long targetValue;

  private Unit unit = Unit.NUMBER;

  private int sequence;

  private Collection<Long> noteIds = new ArrayList<>();
}
