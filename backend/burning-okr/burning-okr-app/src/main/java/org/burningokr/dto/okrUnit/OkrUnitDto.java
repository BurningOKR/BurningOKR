package org.burningokr.dto.okrUnit;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.burningokr.annotation.validation.Base64Image;

import java.util.ArrayList;
import java.util.Collection;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class OkrUnitDto {

  protected Long okrUnitId;

  @NotNull
  @Size(min = 1)
  protected String unitName;

  @Base64Image
  protected String photo; // Represents the image as String

  @NotNull
  @Size(min = 1)
  protected String label;

  protected Collection<Long> objectiveIds = new ArrayList<>();

  public OkrUnitDto(OkrUnitDtoBuilder<?, ?> builder) {
    this.okrUnitId = builder.okrUnitId;
    this.unitName = builder.unitName;
    this.photo = builder.photo;
    this.label = builder.label;
    this.objectiveIds = builder.objectiveIds;
  }

  public OkrUnitDto(OkrUnitDto parent) {
    this.okrUnitId = parent.okrUnitId;
    this.unitName = parent.unitName;
    this.photo = parent.photo;
    this.label = parent.label;
    this.objectiveIds = parent.objectiveIds;
  }
}
