package org.burningokr.dto.okr;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.Collection;

@Data
@Builder
public class ObjectiveDto {

  private Long id;

  @NotNull
  private Long parentUnitId;

  private Long parentObjectiveId;

  @Size(
      max = 255,
      message = "The title of an objective may not be longer than 255 characters.")
  private String title;

  @Size(
      max = 1023,
      message = "The description of an objective is not allowed to be longer than 1023 characters."
  )
  private String description;

  @Size(
      max = 1023,
      message = "The remark of an objective is not allowed to be longer than 1023 characters."
  )
  private String remark;

  @Size(
      max = 2047,
      message = "The review of an objective is not allowed to be longer than 2047 characters."
  )
  private String review;

  private int sequence;


  @Getter(AccessLevel.NONE)
  @Setter(AccessLevel.NONE)
  private boolean isActive;

  private String contactPersonId;
  private Collection<Long> subObjectiveIds = new ArrayList<>();
  private Collection<Long> keyResultIds = new ArrayList<>();
  private Collection<Long> noteIds = new ArrayList<>();

  public boolean hasParentObjectiveId() {
    return parentObjectiveId != null;
  }

  public boolean getIsActive() { return isActive; }
  public void setIsActive(boolean isActive) { this.isActive = isActive; }
}
