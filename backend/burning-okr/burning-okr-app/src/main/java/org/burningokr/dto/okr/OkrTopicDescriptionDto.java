package org.burningokr.dto.okr;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
public class OkrTopicDescriptionDto {
  private Long id;

  @Size(
      min = 1,
      max = 255,
      message = "Name should not be longer than 255 characters."
  )
  private String name;

  private UUID initiatorId;

  @NotNull
  private Collection<UUID> startTeam;

  @NotNull
  private Collection<UUID> stakeholders;

  @Size(
      min = 1,
      max = 1023,
      message = "Description should not be longer than 1024 characters."
  )
  private String description;

  @Size(
      min = 1,
      max = 1023,
      message = "Contributes To should not be longer than 1024 characters."
  )
  private String contributesTo;

  @Size(
      min = 1,
      max = 1023,
      message = "Delimitation should not be longer than 1024 characters."
  )
  private String delimitation;

  private String beginning;

  @Size(
      min = 1,
      max = 1023,
      message = "Dependencies should not be longer than 1024 characters."
  )
  private String dependencies;

  @Size(
      min = 1,
      max = 1023,
      message = "Resources should not be longer than 1024 characters."
  )
  private String resources;

  @Size(
      min = 1,
      max = 1023,
      message = "Handover Over Plan should not be longer than 1024 characters."
  )
  private String handoverPlan;
}
