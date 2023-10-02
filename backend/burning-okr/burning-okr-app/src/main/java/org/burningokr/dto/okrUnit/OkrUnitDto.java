package org.burningokr.dto.okrUnit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.burningokr.annotation.validation.Base64Image;

import java.util.ArrayList;
import java.util.Collection;

@Data
public abstract class OkrUnitDto {

  protected Long OkrUnitId;

  @NotNull
  @Size(min = 1)
  protected String unitName;

  @Base64Image
  protected String photo; // Represents the image as String

  @NotNull
  @Size(min = 1)
  protected String label;

  protected Collection<Long> objectiveIds = new ArrayList<>();
}
